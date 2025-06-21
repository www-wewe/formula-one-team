import sys
from urllib.request import urlopen

def get_token():
    try:
        with urlopen("http://localhost:8080/token") as response:
            return response.read().decode("utf-8")
    except Exception as e:
        print('Not logged in')
        sys.exit(1)