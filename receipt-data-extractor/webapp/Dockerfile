FROM python:3.12.3-alpine3.19
WORKDIR /app
RUN pip install --no-cache-dir install PyPDF2 flask unidecode
COPY . .
COPY receipt-data-extractor/webapp /app
CMD ["python", "receipt_payment_verification.py"]
