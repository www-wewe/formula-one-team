# In this showcase, we simulate the scenario where the user (manager)
# wants to create some initial entities of the F1 team and manage them appropriately.
#
# In particular, the user:
# - create drivers
# - create components
# - get components by type
# - get components by manufacturer 
# - assemble cars from components
# - get cars by make
# - create races
# - get driver by perk
# - get drivers by nationality
# - assign main drivers to cars
# - assign test drivers to cars
# - get cars by main driver
# - assign cars for particular races
# - get races with specific car
# - get races by location
# - update components of cars
# - delete unnecessary component
# - delete dead driver
# - delete broken car
# - delete canceled race

from locust import SequentialTaskSet, task, constant, HttpUser  # pip install locust
from locust.exception import StopUser
from json import dumps, loads
from database.datasets.driver import drivers as drivers_template
from database.datasets.component import engines, gears, spoilers, suspensions
from database.token_utils import get_token
import random

headers = {
    'content-type': 'application/json',
    "Authorization": f"Bearer {get_token()}"
}

components_template = engines + gears + spoilers + suspensions

races_template = [
    {"name": "Grand Prix of Monaco", "location": {"country": "Monaco", "city": "Monte Carlo", "street": "Monte Carlo Circuit"}, "date": "2021-05-23"},
    {"name": "Slovakian Grand Prix", "location": {"country": "Slovakia", "city": "Bratislava", "street": "Slovakia Ring"}, "date": "2021-06-06"},
    {"name": "Czech Grand Prix", "location": {"country": "Czechia", "city": "Brno", "street": "Masaryk Circuit"}, "date": "2021-06-13"},
]

class UserUseCase(SequentialTaskSet):

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.components = []
        self.races = []
        self.cars = []
        self.drivers = []

    def on_start(self):
        """At the beginning, all local lists are cleared"""

        self.components = []
        self.races = []
        self.cars = []
        self.drivers = []

    @task
    def create_drivers(self):
        """Post drivers to the database"""

        for driver in drivers_template:
            with self.client.post(url="http://localhost:8082/drivers",
                                  headers=headers,
                                  catch_response=True,
                                  data=dumps(driver)) as response:

                if response.status_code == 201:
                    response.success()
                    self.drivers.append(loads(response.content))
                else:
                    response.failure(response.json())
                    raise StopUser

    @task
    def create_components(self):
        """Post components to the database"""

        for component in components_template:
            with self.client.post(url="http://localhost:8083/components",
                                  headers=headers,
                                  catch_response=True,
                                  data=dumps(component)) as response:

                if response.status_code == 201:
                    response.success()
                    self.components.append(loads(response.content))
                else:
                    response.failure(response.json())
                    raise StopUser

    @task
    def find_engines(self):
        """Find components by type ENGINE"""

        with self.client.get(url="http://localhost:8083/components?type=ENGINE",
                             headers=headers,
                             catch_response=True) as response:

            if response.status_code == 200:
                response.success()
            else:
                response.failure(response.json())
                raise StopUser

    @task
    def find_mercedes_components(self):
        """Find components with mercedes manufacturer"""

        with self.client.get(url="http://localhost:8083/components?manufacturer=Mercedes",
                             headers=headers,
                             catch_response=True) as response:

            if response.status_code == 200:
                response.success()
            else:
                response.failure(response.json())
                raise StopUser

    @task
    def assemble_cars(self):
        """Post cars to the database"""

        components_ids = [component.get('id') for component in self.components]
        cars_template = [
            {"carMake": "Ferrari", "components": random.sample(components_ids, 3)},
            {"carMake": "Mercedes", "components": random.sample(components_ids, 3)},
            {"carMake": "Red Bull", "components": random.sample(components_ids, 3)},
            {"carMake": "McLaren", "components": random.sample(components_ids, 3)},
            {"carMake": "Renault", "components": random.sample(components_ids, 3)},
            {"carMake": "Honda", "components": random.sample(components_ids, 3)}
        ]

        for car in cars_template:
            with self.client.post(url="http://localhost:8084/cars",
                                  headers=headers,
                                  catch_response=True,
                                  data=dumps(car)) as response:

                if response.status_code == 201 or response.status_code == 200:
                    response.success()
                    self.cars.append(loads(response.content))
                else:
                    response.failure(response.json())
                    raise StopUser

    @task
    def find_ferrari_cars(self):
        """Find cars with Ferrari make"""

        with self.client.get(url="http://localhost:8084/cars?carMake=Ferrari",
                             headers=headers,
                             catch_response=True) as response:

            if response.status_code == 200:
                response.success()
            else:
                response.failure(response.json())
                raise StopUser

    @task
    def create_races(self):
        """Post all races to the database"""

        for race in races_template:
            with self.client.post(url="http://localhost:8081/races",
                                  headers=headers,
                                  catch_response=True,
                                  data=dumps(race)) as response:

                if response.status_code == 201:
                    response.success()
                    self.races.append(loads(response.content))
                else:
                    response.failure(response.json())
                    raise StopUser

    @task
    def find_rain_master_driver(self):
        """Find driver with the most wins in rain"""

        with self.client.get(url="http://localhost:8082/drivers?perk=RAIN_MASTER",
                             headers=headers,
                             catch_response=True) as response:

            if response.status_code == 200:
                response.success()
            else:
                response.failure(response.json())
                raise StopUser

    @task
    def find_german_drivers(self):
        """Find drivers with German nationality"""

        with self.client.get(url="http://localhost:8082/drivers?nationality=German",
                             headers=headers,
                             catch_response=True) as response:

            if response.status_code == 200:
                response.success()
            else:
                response.failure(response.json())
                raise StopUser

    @task
    def assign_main_drivers(self):
        """Assign main drivers for each of created cars"""

        if len(self.drivers) == 0:
            return
        for i in range(len(self.cars)):
            car = {
                "id": self.cars[i].get('id'),
                "mainDriver": self.drivers[0].get('id'),
                "carMake": self.cars[i].get('carMake'),
                "testDrivers": self.cars[i].get('testDrivers'),
                "components": self.cars[i].get('components')
            }

            with self.client.put(url="http://localhost:8084/cars",
                                 headers=headers,
                                 data = dumps(car),
                                 catch_response=True) as response:

                if response.status_code == 200:
                    response.success()
                else:
                    response.failure(response.json())
                    raise StopUser

    @task
    def assign_test_drivers_to_cars(self):
        """Assign test drivers to cars"""

        if (len(self.drivers) < 3):
            return

        for i in range(len(self.cars)):
            car = {
                "id": self.cars[i].get('id'),
                "mainDriver": self.cars[i].get('mainDriver'),
                "carMake": self.cars[i].get('carMake'),
                "testDrivers": [self.drivers[1].get('id'), self.drivers[2].get('id')],
                "components": self.cars[i].get('components')
            }

            with self.client.put(url="http://localhost:8084/cars",
                                 headers=headers,
                                 data=dumps(car),
                                 catch_response=True) as response:

                if response.status_code == 200:
                    response.success()
                else:
                    response.failure(response.json())
                    raise StopUser

    @task
    def find_car_with_specific_main_driver(self):
        """Find car with specific main driver"""

        with self.client.get(url="http://localhost:8084/cars?mainDriver={}"
                             .format(self.drivers[0].get('id')),
                             headers=headers,
                             catch_response=True) as response:

            if response.status_code == 200:
                response.success()
            else:
                response.failure(response.json())
                raise StopUser

    @task
    def assign_cars_to_races(self):
        """Assign cars to races, such that cars Ferrari and McLaren are assigned to all the races"""

        for i in range(len(self.races)):
            with self.client.put(url="http://localhost:8081/races/{}/assignCarOne?carId={}"
                                 .format(self.races[i].get('id'), self.cars[0].get('id')),
                                 headers=headers,
                                 catch_response=True) as response:

                if response.status_code == 200:
                    response.success()
                else:
                    response.failure(response.json())
                    raise StopUser

            with self.client.put(url="http://localhost:8081/races/{}/assignCarTwo?carId={}"
                                 .format(self.races[i].get('id'), self.cars[3].get('id')),
                                 headers=headers,
                                 catch_response=True) as response:

                if response.status_code == 200:
                    response.success()
                else:
                    response.failure(response.json())
                    raise StopUser

    @task
    def find_races_with_car(self):
        """ Find races where specific car participates"""

        with self.client.get(url="http://localhost:8081/races?carId={}"
                             .format(self.cars[0].get('id')),
                             headers=headers,
                             catch_response=True) as response:

            if response.status_code == 200:
                response.success()
            else:
                response.failure(response.json())
                raise StopUser

    @task
    def find_races_by_location(self):
        """ Find racs by location"""
        
        location = {
            "country": "Monaco",
            "city": "Monte Carlo",
            "street": "Monte Carlo Circuit"
        }

        with self.client.get(url="http://localhost:8081/races/location",
                             headers=headers,
                             params=location,
                             catch_response=True) as response:

            if response.status_code == 200:
                response.success()
            else:
                response.failure(response.json())
                raise StopUser

    @task
    def update_components_of_car(self):
        """Update components of car. E.g. engine of Ferrari car (V1) to newer version (V2)"""

        car = {
            "id": self.cars[0].get('id'),
            "mainDriver": self.cars[0].get('mainDriver'),
            "carMake": self.cars[0].get('carMake'),
            "testDrivers": self.cars[0].get('testDrivers'),
            "components": [component.get('id') for component in random.sample(self.components, 3)]
        }

        with self.client.put(url="http://localhost:8084/cars",
                headers=headers,
                data=dumps(car),
                catch_response=True) as response:

            if response.status_code == 200:
                response.success()
            else:
                response.failure(response.json())
                raise StopUser

    @task
    def delete_unnecessary_component(self):
        """Delete unnecessary component"""
        
        with self.client.delete(url="http://localhost:8083/components/{}"
                                .format(self.components[0].get('id')),
                                headers=headers,
                                catch_response=True) as response:

            if response.status_code == 200:
                self.components.pop(0)
                response.success()
            else:
                response.failure(response.json())
                raise StopUser

    @task
    def delete_dead_driver(self):
        """Delete driver when he died"""

        with self.client.delete(url="http://localhost:8082/drivers/{}"
                                .format(self.drivers[0].get('id')),
                                headers=headers,
                                catch_response=True) as response:

            if response.status_code == 200:
                self.drivers.pop(0)
                response.success()
            else:
                response.failure(response.json())
                raise StopUser

    @task
    def delete_broken_car(self):
        """Delete car when it is kaput"""

        with self.client.delete(url="http://localhost:8084/cars/{}"
                                .format(self.cars[0].get('id')),
                                headers=headers,
                                catch_response=True) as response:

            if response.status_code == 200:
                self.cars.pop(0)
                response.success()
            else:
                response.failure(response.json())
                raise StopUser
    
    @task
    def delete_canceled_race(self):
        """ Delete race when it is canceled"""
        
        with self.client.delete(url="http://localhost:8081/races/{}"
                                .format(self.races[0].get('id')),
                                headers=headers,
                                catch_response=True) as response:
            if response.status_code == 200:
                self.races.pop(0)
                response.success()
            else:
                response.failure(response.json())
                raise StopUser


class ApplicationUser(HttpUser):
    tasks = [UserUseCase]
    wait_time = constant(1)
