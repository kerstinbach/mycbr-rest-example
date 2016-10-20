import json
from pprint import pprint

institutes = []

def parse_api():
	# stores the api as a json object
	with open("../data/json/org_units.json") as data_file:
		data = json.load(data_file)

	# defines the path to where the faculties start in the api
	path_to_faculties = data["orgUnit"][0]["subUnit"][0]["subUnit"]

	# iterating over all faculties
	for i in range(9, 16):
		num_institutes = len(path_to_faculties[i]["subUnit"])
		for j in range(0, num_institutes):
			if path_to_faculties[i]["subUnit"][j]["category"] == "Institutt":
				institute = {}
				name = path_to_faculties[i]["subUnit"][j]["name"]
				acronym = path_to_faculties[i]["subUnit"][j]["acronym"]

				institute["name"] = acronym + " - " + name
				institutes.append(institute) 


def to_json():
	json_data = json.dumps(institutes)

	target = open("data/institutes.json", "w")

	target.write(json_data)

	target.close()

def write_all_institutes_to_file():
	with open("../attributes/institutes.txt", 'w') as file:
		for line in institutes:
			institute = line["name"] +'\n'
			file.write(institute)
		file.close()


parse_api()
to_json()
#write_all_institutes_to_file()







