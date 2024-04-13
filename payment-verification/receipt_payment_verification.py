import os
import re
import psycopg2
from io import BytesIO
from unidecode import unidecode
from flask import Flask, request, jsonify

app = Flask(__name__)

UPLOAD_FOLDER = ''
ALLOWED_EXTENSIONS = {'pdf'}

app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

conn = psycopg2.connect(
    dbname="fp",
    user="postgres",
    password="GFmyjF3eN6d5",
    host="localhost",
    port="5432"
)


def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


def extract_data_from_pdf(pdf_path):
    with open(pdf_path, 'rb') as file:
        return file.read()


def verify_data(text):
    cursor = conn.cursor()
    cursor.execute("SELECT pattern FROM banks WHERE bank_name = 'SberBank'")
    pattern = cursor.fetchone()[0]
    cursor.close()
    match = re.match(pattern, text, re.MULTILINE | re.DOTALL)
    if match:
        return match.groups()
    return None


@app.route('/payment-verification/receipt', methods=['POST'])
def upload_receipt():
    if 'file' not in request.files:
        return jsonify({'error': 'No file part'})

    file = request.files['file']

    if file.filename == '':
        return jsonify({'error': 'No selected file'})

    if file and allowed_file(file.filename):
        filename = file.filename
        file_path = os.path.join(app.config['UPLOAD_FOLDER'], filename)
        file.save(file_path)

        # Verify file size
        if os.path.getsize(file_path) > 256 * 1024:
            os.remove(file_path)
            return jsonify({'error': 'File size exceeds 256 KB limit'})

        # Extract data from PDF
        file_data = extract_data_from_pdf(file_path)

        # Save the receipt PDF data to the database
        cursor = conn.cursor()
        cursor.execute("INSERT INTO receipts (file_data) VALUES (%s) RETURNING id", (psycopg2.Binary(file_data),))
        receipt_id = cursor.fetchone()[0]
        conn.commit()
        cursor.close()

        # Verify data integrity
        data = [unidecode(entry) for entry in verify_data(file_data)]

        if data is not None:
            # Save extracted data to receipt_payment_verification table
            cursor = conn.cursor()
            cursor.execute("INSERT INTO receipt_payment_verification VALUES (%s, %s, %s, %s)",
                           (data[0], data[1], data[2], data[3]))
            conn.commit()
            cursor.close()
            # Update payment status in payments table
            cursor = conn.cursor()
            cursor.execute("UPDATE payments SET status = 'verified' WHERE status = 'verifying'")
            conn.commit()
            cursor.close()
            return jsonify({'success': True, 'data': data, 'receipt_id': receipt_id})
        else:
            # Save failed verification
            cursor = conn.cursor()
            cursor.execute("INSERT INTO failed_verification VALUES (%s)", (filename,))
            conn.commit()
            cursor.close()
            os.remove(file_path)
            return jsonify({'error': 'Payment was not verified'}), 403

    return jsonify({'error': 'Invalid file format'})


if __name__ == '__main__':
    app.run(debug=True)
