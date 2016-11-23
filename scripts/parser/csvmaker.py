file = open("institutes.txt", 'r').readlines()
new_file = open("institutes.py", 'w')


institutes = []

for line in file:
	institutes.append(line.strip())


print(institutes)