import requests
from token_utils import get_token

headers = {
    "Authorization": f"Bearer {get_token()}"
}

def clear_components():
    url = "http://localhost:8083/components"
    response = requests.delete(url, headers=headers)
    if response.status_code == 200: 
        print("Components cleared")
    else: 
        print(f"Failed to clear components: {response.status_code}")

def clear_drivers():
    url = "http://localhost:8082/drivers"
    response = requests.delete(url, headers=headers)
    if response.status_code == 200:
        print("Drivers cleared")
    else:
        print(f"Failed to clear drivers: {response.status_code}")

def clear_cars():
    url = "http://localhost:8084/cars"
    response = requests.delete(url, headers=headers)
    if response.status_code == 200:
        print("Cars cleared")
    else:
        print(f"Failed to clear cars: {response.status_code}")

def clear_races():
    url = "http://localhost:8081/races"
    response = requests.delete(url, headers=headers)
    if response.status_code == 200:
        print("Races cleared")
    else:
        print(f"Failed to clear races: {response.status_code}")

if __name__ == "__main__":
    clear_components()
    clear_drivers()
    clear_cars()
    clear_races()