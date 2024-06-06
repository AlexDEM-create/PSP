FROM python:3.12.3-alpine3.19

WORKDIR /app

COPY . /app

WORKDIR /app/webapp

RUN pip install --no-cache-dir -r /app/requirements.txt

EXPOSE 8080

ENV FLASK_APP=receipt_payment_verification.py

CMD ["flask", "run", "--host=0.0.0.0"]
