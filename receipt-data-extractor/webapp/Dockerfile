FROM python:3.12.3-alpine3.19
WORKDIR /app
COPY .. /app
RUN pip install PyPDF2 flask unidecode
COPY receipt-data-extractor/webapp/receipt_payment_verification.py receipt_payment_verification.py
EXPOSE 8080
CMD ["python", "receipt_payment_verification.py"]
