FROM python:3.12.3-alpine3.19

WORKDIR /app

COPY receipt-data-extractor/webapp /app

RUN python3 -m ensurepip && pip install --no-cache --upgrade pip setuptools virtualenv

RUN python3 -m venv /app/venv

RUN /app/venv/bin/pip install --no-cache-dir -r requirements.txt

EXPOSE 8080

ENV VIRTUAL_ENV=/app/venv
ENV PATH="/app/venv/bin:$PATH"

CMD ["python3", "receipt_payment_verification.py"]
