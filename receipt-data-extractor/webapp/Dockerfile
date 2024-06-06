FROM python:3.12.3-alpine3.19

WORKDIR /app

COPY receipt-data-extractor/webapp /app

RUN pip install --no-cache-dir -r requirements.txt

RUN pip list

ENV PYTHONPATH=/usr/local/lib/python3.12/site-packages

EXPOSE 8080

CMD ["python", "receipt_payment_verification.py"]
