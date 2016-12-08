def generate_dictionary():
	target = open('instituteInfo.py', 'w')
	target.write("institutes = [" + '\n')
	counter = 0
	with open('institutes.txt', 'r') as file:
		for line in file:
			line = line.strip('\n')
			acronym = "'" + str(line.split('-')[1].strip(" ")) + "'"
			name = "'" + str(line.split('-')[2].strip(" ")) + "'"
			faculty = "'" + str(line.split('-')[0].strip(" ")) + "'"
			target.write("{'acronym': " + acronym + ', ' + "'name': " + name + ', ' + "'faculty': " + faculty + '}')
			if counter == 49:
				target.write("\n")
			else:
				target.write("," + "\n")
			counter += 1

	target.write(']')
	target.close()
	file.close()

if __name__ == '__main__':
	generate_dictionary()