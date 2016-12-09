import urllib.request as urllib
import time

url = "https://www.ntnu.no/studier/studier_i_utlandet/rapport/report.php?recordid="

num_retrieved = 0
num_failed = 0
num_remaining = 10000
for i in range(0, 9999):

	try:
		urllib.urlretrieve(url+str(i), "more_retrieved_html_files/file" + str(i) + ".html")
		num_retrieved += 1
	except (urllib.HTTPError):
		num_failed += 1
		pass

	num_remaining -= 1


	print('-'*50)
	print("Num retrieved documents: " + str(num_retrieved))
	print("Num failed documents: " + str(num_failed))
	print("Num remaining documents: " + str(num_remaining))
	print('-'*50)

	time.sleep(6)