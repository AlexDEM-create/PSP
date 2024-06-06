FROM python:3.12.3-alpine3.19

WORKDIR /app

COPY receipt-data-extractor/webapp /app

RUN python3 -m pip cache purge

RUN python3 -m pip install --no-cache-dir PyPDF2
RUN python3 -m pip install --no-cache-dir flask unidecode

EXPOSE 8080

CMD ["python3", "receipt_payment_verification.py"]
