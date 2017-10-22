#!/usr/bin/env python

import requests
from csv import writer
from datetime import datetime
from os import path

pair = 'XBTEUR'
interval_minutes = 5
csv_file_name = '{}-{}-{}.csv'.format(pair, interval_minutes, datetime.utcnow().strftime('%Y%m%d-%H:%M:%S'))

pair_result_mapping = {
    'XBTEUR': 'XXBTZEUR'
}

url = 'https://api.kraken.com/0/public/OHLC'
payload = {
    'pair': pair,
    'interval': interval_minutes,
}

request = requests.get(url, params=payload)

if (request.status_code != 200):
    print('Response code: {}'.format(request.status_code))
    exit(1)

response = request.json()

errors = response.get('error')
if len(errors) > 0:
    for error in errors:
        print(error)
    exit(1)

result = response.get('result').get(pair_result_mapping.get(pair))

csv_file_path = path.join('data', csv_file_name)

with open(csv_file_path, 'w') as f:
    writer = writer(f)
    for line in result:
        writer.writerow(line)
