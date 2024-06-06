FROM python:3.11-alpine3.20

WORKDIR /app

RUN apk update
RUN apk add py3-pypdf2

COPY receipt-data-extractor/webapp /app

RUN python3 -m pip install --no-cache-dir flask unidecode

RUN whoami

EXPOSE 8080

CMD ["python3", "receipt_payment_verification.py"]
