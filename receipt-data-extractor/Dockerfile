FROM python:3.12.3-alpine3.19
WORKDIR /app
COPY . /app
RUN pip install PyPDF2 flask unidecode
EXPOSE 8080
CMD ["python", "receipt_payment_verification.py"]
