FROM python:3.12.3-alpine3.19 AS build
WORKDIR /app
COPY . .

FROM python:3.12.3-alpine3.19
WORKDIR /app
COPY --from=build /app/receipt-data-extractor/webapp/receipt_payment_verification.py receipt_payment_verification.py
ENV PYTHONPATH="$PYTHONPATH:/app"
RUN pip install PyPDF2 flask unidecode
EXPOSE 8080
CMD ["python", "receipt_payment_verification.py"]
