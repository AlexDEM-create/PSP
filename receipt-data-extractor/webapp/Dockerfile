FROM python:3.12.3-alpine3.19

RUN pip3 install -r requirements.txt

WORKDIR /app

COPY receipt-data-extractor/webapp /app

EXPOSE 8080

CMD ["python3", "receipt_payment_verification.py"]
