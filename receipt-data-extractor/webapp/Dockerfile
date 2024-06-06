FROM python:3.12.3-alpine3.19

WORKDIR /app

COPY receipt-data-extractor/webapp /app

RUN python3 -m pip install --no-cache-dir -r requirements.txt

EXPOSE 8080

CMD ["python3", "receipt_payment_verification.py"]
