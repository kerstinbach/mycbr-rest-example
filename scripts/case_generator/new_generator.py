import json
import random as r

conts = ["Asia", "Europe", "South America", "North America", "Oceania", "Africa"]
file = open("attributes/countries.txt", 'w')

with open("data/json/countries.json", 'r') as json_file:
	data = json.load(json_file)

	rand_land = r.choice(data[r.choice(conts)])

	print(rand_land["name"])

