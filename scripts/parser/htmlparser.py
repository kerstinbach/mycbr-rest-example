import urllib.request as urllib
from html.parser import HTMLParser
import data.countryinfo as ci
import data.universityinfo as ui
from fuzzywuzzy import fuzz
from fuzzywuzzy import process
import json
import glob
from titlecase import titlecase
import re

data_list = []
star_list = []
institutes = []

cases = []


class Parser(HTMLParser):

    def __init__(self):
        HTMLParser.__init__(self)

    def destroy(self):
        del self

    def handle_data(self, data):
        global data_list
        data_list.append(data)

    def handle_starttag(self, tag, attrs):
        if attrs == [('class', 'fa fa-star-o'), ('aria-hidden', 'true')] or attrs == [('class', 'fa fa-star'), ('aria-hidden', 'true')] or attrs == [('class', 'fa fa-star')] or attrs == [('class', 'fa fa-star-o')]:
            star_list.append(attrs)


def get_html():
    url = "Internasjonal Seksjon.html"
    result = urllib.urlopen(url)
    return result.read().decode("utf-8").replace('\r\n', ' ')


def parse_html():
    data_object = {}
    data_object["courses"] = []
    for i in range(0, len(data_list)):
        element = data_list[i]
        if element == "Land:":
            next_element = data_list[i+1]
            data_object['country'] = next_element
            data_object['continent'] = get_continent(next_element)

        elif element == "Vertsinstitusjon:":
            next_element = data_list[i+1]
            data_object['university'] = format_university(next_element)

        elif element == "Institutt hjemme:":
            next_element = data_list[i+1]
            data_object['institute'] = format_institute(next_element)

        elif element == "Utvekslingsperiode:":
            next_element = data_list[i+1]
            data_object['study_period'] = format_study_period(next_element)

        elif element == "Studietype:":
            next_element = data_list[i+1]
            data_object['language'] = format_language(next_element)

        elif element == "Fag 1:":
            next_element = data_list[i+1]
            if next_element[0].isalpha():
                data_object["courses"].append(format_course(next_element))

        elif element == "Fag 2:":
            next_element = data_list[i+1]
            if next_element[0].isalpha():
                data_object["courses"].append(format_course(next_element))

        elif element == "Fag 3:":
            next_element = data_list[i+1]
            if next_element[0].isalpha():
                data_object["courses"].append(format_course(next_element))

        elif element == "Fag 4:":
            next_element = data_list[i+1]
            if next_element[0].isalpha():
                data_object["courses"].append(format_course(next_element))

        elif element == "Fag 5:":
            next_element = data_list[i+1]
            if next_element[0].isalpha():
                data_object["courses"].append(format_course(next_element))

        elif element == "Fag 6:":
            next_element = data_list[i+1]
            if next_element[0].isalpha():
                data_object["courses"].append(format_course(next_element))

        elif element == "Fag 7:":
            next_element = data_list[i+1]
            if next_element[0].isalpha():
                data_object["courses"].append(format_course(next_element))

        elif element == "Fag 8:":
            next_element = data_list[i+1]
            if next_element[0].isalpha():
                data_object["courses"].append(next_element)

        data_object["academic_quality"] = get_rating("academic")
        data_object["social_quality"] = get_rating("social")

    return data_object


def format_study_period(s):
    return int(re.match(r'.*([1-3][0-9]{3})', s).group(1))


def format_language(s):
    if s == "Fag på vertsinstitusjonens undervisningsspråk":
        return "Ukjent"
    elif not s[0].isalpha() or s == "":
        return "Ukjent"
    else:
        return s.split()[-1].title()
    
    return "Engelsk"


def get_continent(s):
    if s.lower() == "usa":
        return "North America"
    else :
        for c in ci.countries:
            if c['name'].lower() == s.lower():
                return c['continent'].title()


def get_institutes():
    file = open("data/institutes.txt", 'r')
    for line in file.readlines():
        institutes.append(line.strip())
    file.close()


def format_institute(s):
    best_match = {'name': "", 'score': 0}
    for institute in institutes:
        if fuzz.ratio(s, institute) > best_match['score']:
            best_match['name'] = institute
            best_match['score'] = fuzz.ratio(s, institute)
    return best_match['name']


def format_university(s):
    if s.isupper():
        for u in ui.universities:
            if u['acronym'].lower() == s.lower():
                return decodeString(titlecase(u['name']))
        return decodeString(s)
    else:
        return decodeString(titlecase(s))


def format_course(s):
    s = s.replace('!', '')
    first_part = s.split()[0]
    for char in first_part:
        if char.isdigit():
            return s
    return "XXXX" + " " + s


def get_rating(s):
    temp = []
    star_counter = 0
    if s == "academic":
        for i in range(181, 186):
            temp.append(star_list[i])

        for row in temp:
            if row[0][1] == "fa fa-star":
                star_counter += 1

        return star_counter

    elif s == "social":
        for i in range(136, 141):
            temp.append(star_list[i])

        for row in temp:
            if row[0][1] == "fa fa-star":
                star_counter += 1

        return star_counter

    else:
        return None

def run(s):
    global data_list
    global star_list
    html = open(s, 'r')
    P = Parser()
    P.feed(html.read())
    html.close()
    obj = parse_html()
    P.destroy()
    P.close()
    data_list = []
    star_list = []
    return obj


def make_json():
    output = open('output/cases.json', 'w')
    with open('output/cases.json', 'w') as output:
        json.dump(cases, output)
    output.close()


def print_cases():
    for case in cases:
        for k, v in case.items():
            print(k + ':', v)
        print('-'*100)

def test_run():
    test_file_list = ["file1.html", "file2.html", "file3.html"]

    for file in test_file_list:
        cases.append(run("test_html_files/" + file))


def decodeString(s):
    return s.encode('utf-8').decode('utf-8')


def real_run():
    data_list = []
    star_list = []
    file_list = glob.glob("retrieved_html_files/*.html")
    counter = 0
    stopper = 0
    for file in file_list:
        case = run(file)
        if not case["courses"]:
            continue
        #if stopper >= 50:
        #    return
        cases.append(case) 
        counter += 1
        stopper += 1
        print("Finished: " + str(file) + " (" + str(counter) + '/' + "3644)")



if __name__ == "__main__":
    get_institutes()
    real_run()
    #test_run()
    #print_cases()
    make_json()

    