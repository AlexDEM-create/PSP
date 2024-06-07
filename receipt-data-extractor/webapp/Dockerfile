FROM python:3.12.3-alpine3.19

WORKDIR /app

COPY receipt-data-extractor/webapp /app

RUN pip install pipenv && pipenv requirements >> requirements.txt && pip install -r requirements.txt

EXPOSE 8080

CMD ["sh", "-c", "python", "receipt_payment_verification.py"]
