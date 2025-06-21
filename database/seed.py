import requests
from datasets.component import engines, gears, spoilers, suspensions
from datasets.driver import drivers
from datasets.car import generate_cars
from datasets.race import generate_races
from token_utils import get_token

headers = {
    "Authorization": f"Bearer {get_token()}"
}

engines_ids = []
gears_ids = []
spoilers_ids = []
suspensions_ids = []

driver_ids = []
car_ids = []

def seed_components():
    url = "http://localhost:8083/components"
    for engine in engines:
        response = requests.post(url, json=engine, headers=headers)
        if response.status_code == 201:
            engines_ids.append(response.json()['id'])
            print(f"Seeded engine: {engine}")
        else:
            print(f"Failed to seed engine: {engine}")

    for gear in gears:
        response = requests.post(url, json=gear, headers=headers)
        if response.status_code == 201:
            gears_ids.append(response.json()['id'])
            print(f"Seeded gear: {gear}")
        else:
            print(f"Failed to seed gear: {gear}")

    for spoiler in spoilers:
        response = requests.post(url, json=spoiler, headers=headers)
        if response.status_code == 201:
            spoilers_ids.append(response.json()['id'])
            print(f"Seeded spoiler: {spoiler}")
        else:
            print(f"Failed to seed spoiler: {spoiler}")

    for suspension in suspensions:
        response = requests.post(url, json=suspension, headers=headers)
        if response.status_code == 201:
            suspensions_ids.append(response.json()['id'])
            print(f"Seeded suspension: {suspension}")
        else:
            print(f"Failed to seed suspension: {suspension}")

def seed_drivers():
    url = "http://localhost:8082/drivers"
    for driver in drivers:
        response = requests.post(url, json=driver, headers=headers)
        if response.status_code == 201:
            driver_ids.append(response.json()['id'])
            print(f"Seeded driver: {driver}")
        else:
            print(f"Failed to seed driver: {driver}")

def seed_cars():
    cars = generate_cars(driver_ids, engines_ids, gears_ids, spoilers_ids, suspensions_ids)
    url = "http://localhost:8084/cars"
    for car in cars:
        response = requests.post(url, json=car, headers=headers)
        if response.status_code == 201:
            car_ids.append(response.json()['id'])
            print(f"Seeded car: {car}")
        else:
            print(f"Failed to seed car: {car}")

def seed_races():
    races = generate_races(car_ids)
    url = "http://localhost:8081/races"
    for race in races:
        response = requests.post(url, json=race, headers=headers)
        if response.status_code == 201:
            print(f"Seeded race: {race}")
        else:
            print(f"Failed to seed race: {race}")

if __name__ == "__main__":
    seed_components()
    seed_drivers()
    seed_cars()
    seed_races()
