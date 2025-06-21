# This file contains the dataset for the race collection

race_names = ["Grand Prix of Monaco", "British Grand Prix", "Italian Grand Prix", "Japanese Grand Prix", "Belgian Grand Prix", "Slovakian Grand Prix", "Czech Grand Prix"]
race_location = [
    {"country": "Monaco", "city": "Monte Carlo", "street": "Circuit de Monaco"},
    {"country": "United Kingdom", "city": "Silverstone", "street": "Silverstone Circuit"},
    {"country": "Italy", "city": "Monza", "street": "Autodromo Nazionale Monza"},
    {"country": "Japan", "city": "Suzuka", "street": "Suzuka International Racing Course"},
    {"country": "Belgium", "city": "Spa", "street": "Circuit de Spa-Francorchamps"},
    {"country": "Slovakia", "city": "Bratislava", "street": "Slovakia Ring"},
    {"country": "Czech Republic", "city": "Brno", "street": "Brno Circuit"}
]

def generate_races(car_ids):
    races = []
    max_length = min(len(car_ids) - 1, len(race_names))
    for i in range(max_length):
        if i == len(car_ids):
            break
        races.append({"name": race_names[i], "location": race_location[i], "date": "2024-12-24" ,"car1": car_ids[i], "car2": car_ids[i + 1]})
    return races
