FROM python:3.12.3-alpine3.19 AS build
WORKDIR /app
COPY . .
RUN pip install --no-cache-dir PyPDF2 flask unidecode -t /app/packages

FROM python:3.12.3-alpine3.19
ENV PYTHONPATH=/app/packages
WORKDIR /app
COPY --from=build /app/receipt-data-extractor/webapp/receipt_payment_verification.py receipt_payment_verification.py
COPY --from=build /app/packages /usr/local/lib/python3.12/site-packages
EXPOSE 8080
CMD ["python", "receipt_payment_verification.py"]
