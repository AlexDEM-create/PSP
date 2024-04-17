import PyPDF2
import re
from flask import Flask, request, jsonify
from unidecode import unidecode

app = Flask(__name__)

ALLOWED_EXTENSIONS = {'pdf'}


def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


def extract_data_from_pdf(pdf_file):
    pdf_reader = PyPDF2.PdfReader(pdf_file)
    return pdf_reader.pages[0].extract_text()


def verify_data(patterns, text):
    for pattern in patterns:
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

    if 'patterns' not in request.form:
        return jsonify({'error': 'No patterns part'})

    patterns = request.form.getlist('patterns')

    if file and allowed_file(file.filename):
        # Verify file size
        if file.content_length > 256 * 1024:
            return jsonify({'error': 'File size exceeds 256 KB limit'})

        # Extract data from PDF
        file_data = extract_data_from_pdf(file)

        # Verify data integrity
        data = [unidecode(entry) for entry in verify_data(patterns, file_data)]

        if data is not None:
            return jsonify(data)
        else:
            return jsonify({'error': 'Payment was not verified'}), 403

    return jsonify({'error': 'Invalid file format'})


if __name__ == '__main__':
    app.run(debug=True)
