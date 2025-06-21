import random

car_makes = ["Ferrari", "Mercedes", "Red Bull", "McLaren", "Renault", "Honda"]

def generate_cars(driver_ids, engines_ids, gears_ids, spoilers_ids, suspensions_ids):
    cars = []
    max_length = min(len(driver_ids) - 1, len(engines_ids), len(gears_ids), len(spoilers_ids), len(suspensions_ids))
    for i in range(max_length):
        cars.append({
            "mainDriver": driver_ids[i],
            "carMake": random.choice(car_makes),
            "testDrivers": [driver_ids[i + 1], driver_ids[i + 2]],
            "components": [engines_ids[i], gears_ids[i], spoilers_ids[i], suspensions_ids[i]]
            })
    return cars

