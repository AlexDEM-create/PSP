FROM python:3.8-slim

RUN pip install PyPDF2 flask unidecode

WORKDIR /app

COPY receipt-data-extractor/webapp /app

EXPOSE 8080

CMD ["python3", "receipt_payment_verification.py"]
