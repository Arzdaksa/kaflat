import requests

#####################################################################################
def send_to_server(method, params):
    url = "http://localhost:9514/test_[replace]_method"
    response = None
    url = url.replace('[replace]', method.lower())

    try:
        if method == 'GET':
            response = requests.get(url, params=params)
        elif method == 'POST':
            response = requests.post(url, json=params)
        elif method == 'PUT':
            response = requests.put(url, json=params)
        elif method == 'DELETE':
            response = requests.delete(url, params=params)

        return response

    except requests.exceptions.ConnectionError:
        print("Error: Could not connect. Is your local server running on port 9514?")
        return None

def validate_res(res):
    if res is None:
        print("Something went wrong res is None")
    elif res.status_code == 200:
        print("GET Responce recieved:")
        print(res.text)
    elif res.status_code == 201:
        print('POST Response recieved:')
        print(res.json()['message'])
    else:
        print("Responce error: " + str(res.status_code) + str(res.text))

####################################################################################
query_params1 = {
    'id':325149904,
    'year':2025
}

## first task - GET
get_res = send_to_server('GET', query_params1)
validate_res(get_res)
##

## second task - POST
query_params2 = query_params1
query_params2['requestId'] = str(get_res.text)

post_res = send_to_server('POST', query_params2)
validate_res(post_res)
##

## third task - PUT
query_params3 = query_params2
query_params3['year'] = 2014 #changed a parameter

put_res = send_to_server('PUT', query_params3)
validate_res(put_res)
##

## forth task - DELETE
query_params4 = {'id':325149904}
delete_res = send_to_server('DELETE', query_params4)
validate_res(delete_res)
##

