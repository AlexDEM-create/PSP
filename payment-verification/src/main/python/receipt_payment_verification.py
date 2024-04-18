import os
import re
from flask import Flask, request, jsonify
from unidecode import unidecode

app = Flask(__name__)

ALLOWED_EXTENSIONS = {'pdf'}

def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


def extract_data_from_pdf(pdf_path):
    with open(pdf_path, 'rb') as file:
        return file.read()


def verify_data(pattern, text):
    match = re.match(pattern, text, re.MULTILINE | re.DOTALL)
    if match:
        return match.groups()
    return None


@app.route('/payment-verification/receipt/extract-data', methods=['POST'])
def upload_receipt():
    if 'file' not in request.files:
        return jsonify({'error': 'No file part'})

    file = request.files['file']

    if file.filename == '':
        return jsonify({'error': 'No selected file'})

    if 'pattern' not in request.files:
        return jsonify({'error': 'No pattern part'})

    pattern = request.files['pattern']

    if 'folder' not in request.files:
        return jsonify({'error': 'No folder part'})

    folder = request.files['folder']

    if file and allowed_file(file.filename):
        filename = file.filename
        file_path = os.path.join(folder, filename)
        file.save(file_path)

        # Verify file size
        if os.path.getsize(file_path) > 256 * 1024:
            os.remove(file_path)
            return jsonify({'error': 'File size exceeds 256 KB limit'})

        # Extract data from PDF
        file_data = extract_data_from_pdf(file_path)

        # Verify data integrity
        data = [unidecode(entry) for entry in verify_data(pattern, file_data)]

        if data is not None:
            return jsonify(data)
        else:
            return jsonify({'error': 'Payment was not verified'}), 403

    return jsonify({'error': 'Invalid file format'})


if __name__ == '__main__':
    app.run(debug=True)
