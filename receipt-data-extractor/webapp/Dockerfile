FROM python:3.12.3-alpine3.19 AS build
WORKDIR /app
COPY . .
RUN pip install PyPDF2 flask unidecode

FROM python:3.12.3-alpine3.19
WORKDIR /app
RUN pip install PyPDF2 flask unidecode
COPY --from=build /app/receipt-data-extractor/webapp/receipt_payment_verification.py receipt_payment_verification.py
EXPOSE 8080
ENTRYPOINT ["python", "receipt_payment_verification.py"]
