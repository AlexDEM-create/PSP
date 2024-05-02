import PyPDF2
import logging
import re
from flask import Flask, request, jsonify
from unidecode import unidecode

app = Flask(__name__)

ALLOWED_EXTENSIONS = {'pdf'}

file_handler = logging.FileHandler('record.log')
file_handler.setLevel(logging.DEBUG)

formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
file_handler.setFormatter(formatter)

logger = logging.getLogger(__name__)
logger.setLevel(logging.DEBUG)

logger.addHandler(file_handler)


def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


def extract_data_from_pdf(pdf_file):
    pdf_reader = PyPDF2.PdfReader(pdf_file)
    return pdf_reader.pages[0].extract_text()


def verify_data(patterns, text):
    for pattern in patterns:
        match = re.match(pattern, text, re.MULTILINE | re.DOTALL)
        if match:
            return {key: unidecode(value) for key, value in match.groupdict().items()}
    return None


@app.route('/payment-verifications/receipts/extract-data', methods=['POST'])
def upload_receipt():
    if 'file' not in request.files:
        return jsonify({'error': 'No file part'}), 403

    file = request.files['file']

    if file.filename == '':
        return jsonify({'error': 'No selected file'}), 403

    if 'patterns' not in request.form:
        return jsonify({'error': 'No patterns part'}), 403

    patterns = request.form.getlist('patterns')

    if file and allowed_file(file.filename):
        if file.content_length > 256 * 1024:
            return jsonify({'error': 'File size exceeds 256 KB limit'}), 403

        file_data = extract_data_from_pdf(file)

        data = verify_data(patterns, file_data)

        if data is not None:
            return jsonify(data)
        else:
            return jsonify({'error': 'Payment was not verified'}), 403

    return jsonify({'error': 'Invalid file format'}), 403


if __name__ == '__main__':
    app.run(debug=True, port=8080)
