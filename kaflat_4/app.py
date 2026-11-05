import logging
import time

from flask import Flask, request, jsonify
import loggers

app = Flask(__name__)

request_logger = loggers.RequestLogger()
book_logger = loggers.BookLogger()
loggers_dict = {
    request_logger.name():request_logger,
    book_logger.name():book_logger
    }

#runtime database
books = {}
next_id = 1

#genres
VALID_GENRES = {"SCI_FI", "NOVEL", "HISTORY", "MANGA", "ROMANCE", "PROFESSIONAL"}


#return an ok message to client
def ok(result):
    return jsonify({"result": result}), 200


#return an error message to client
def error(status_code, message):
    request_logger.log_error(message, status_code)
    return jsonify({"errorMessage": message}), status_code


#check if server live
@app.route("/books/health", methods=["GET"])
def health():
    request_logger.log_info('GET')
    request_logger.log_debug(str(time.perf_counter()))
    return "OK", 200


#create a new book
@app.route("/book", methods=["POST"])
def create_book():
    #log
    request_logger.log_info('POST')
    start = time.perf_counter()

    global next_id
    data = request.get_json()  # parse the JSON body the client sent
    if type(data) is None:
        return error(409, 'missing params')

    title = data.get("title", "")
    author = data.get("author", "")
    year = data.get("year")
    price = data.get("price")
    genres = data.get("genres", [])

    # --- Validation ---
    # Year must be in range 1940-2100
    if not (1940 <= year <= 2100):
        return error(409, f"Error: Can't create new Book that its year [{year}] is not in the accepted range [1940 -> 2100]")

    # Price must be positive
    if price <= 0:
        return error(409, "Error: Can't create new Book with negative price")

    # Title must be unique
    title_lower = title.lower()
    for book in books.values():
        if book["title"].lower() == title_lower:
            return error(409, f"Error: Book with the title [{title}] already exists in the system")

    # --- All good: create the book ---
    #-- book log --
    book_logger.log_info(f'Creating new Book with Title [{title}]')
    book_logger.log_debug(f'Currently there are {len(list(books.values()))} Books in the system. New Book will be assigned with id {next_id}')

    book_id = next_id
    next_id += 1

    books[book_id] = {
        "id": book_id,
        "title": title,
        "author": author,
        "year": year,
        "price": price,
        "genres": genres,
    }

    request_logger.log_debug(str(time.perf_counter() - start))

    return ok(book_id)


#go over request and set filters
def apply_filters(args):
    # --- get book database ---
    filtered = list(books.values())

    # --- get books with same author ---
    author = args.get("author")
    if author:
        author_lower = author.lower()
        filtered = [b for b in filtered if b["author"].lower() == author_lower]

    # --- numeric filters ---
    def get_int_param(name):
        val = args.get(name)
        if val is None:
            return None, None
        try:
            return int(val), None
        except ValueError:
            return None, error(400, f"Bad request: {name} must be numeric")

    price_gte, err = get_int_param("price-bigger-than")
    if err:
        return None, err
    price_lte, err = get_int_param("price-less-than")
    if err:
        return None, err
    year_gte, err = get_int_param("year-bigger-than")
    if err:
        return None, err
    year_lte, err = get_int_param("year-less-than")
    if err:
        return None, err

    if price_gte is not None:
        filtered = [b for b in filtered if b["price"] >= price_gte]
    if price_lte is not None:
        filtered = [b for b in filtered if b["price"] <= price_lte]
    if year_gte is not None:
        filtered = [b for b in filtered if b["year"] >= year_gte]
    if year_lte is not None:
        filtered = [b for b in filtered if b["year"] <= year_lte]

    # --- genres filter ---
    genres_param = args.get("genres")
    if genres_param:
        requested = [g.strip() for g in genres_param.split(",")]
        for g in requested:
            if g not in VALID_GENRES:
                return None, error(400, f"Bad request: invalid genre '{g}'")

        filtered = [b for b in filtered if any(g in b["genres"] for g in requested)]

    return filtered, None


#/books/total, get the number of books that fit the filters
@app.route("/books/total", methods=["GET"])
def get_total():
    #log
    request_logger.log_info('GET')
    start = time.perf_counter()

    filtered, err = apply_filters(request.args)
    if err:
        return err

    total = len(filtered)

    book_logger.log_info(f'Total Books found for requested filters is {total}')
    request_logger.log_debug(str(time.perf_counter() - start))
    return ok(total)


#get the books that fit the filters
@app.route("/books", methods=["GET"])
def get_books():
    #log
    request_logger.log_info('GET')
    start = time.perf_counter()

    filtered, err = apply_filters(request.args)
    if err:
        return err
    # Sort by title, case-insensitive, ascending (A -> Z)
    sorted_books = sorted(filtered, key=lambda b: b["title"].lower())

    book_logger.log_info(f'Total Books found for requested filters is {len(sorted_books)}')
    request_logger.log_debug(str(time.perf_counter() - start))

    return ok(sorted_books)


#get a single book
@app.route("/book", methods=["GET"])
def get_book():
    #log
    request_logger.log_info('GET')
    start = time.perf_counter()

    book_id = request.args.get("id")
    try:
        book_id = int(book_id)
    except (TypeError, ValueError):
        return error(400, "Bad request: id must be numeric")

    book = books.get(book_id)
    if book is None:
        return error(404, f"Error: no such Book with id {book_id}")

    book_logger.log_debug(f'Fetching book id {book_id} details')
    request_logger.log_debug(str(time.perf_counter() - start))

    return ok(book)


#update a book price
@app.route("/book", methods=["PUT"])
def update_price():
    #log
    request_logger.log_info('PUT')
    start = time.perf_counter()

    try:
        book_id = int(request.args.get("id"))
        new_price = int(request.args.get("price"))
    except (TypeError, ValueError):
        return error(400, "Bad request: id and price must be numeric")

    book = books.get(book_id)
    if book is None:
        return error(404, f"Error: no such Book with id {book_id}")

    if new_price <= 0:
        return error(409, f"Error: price update for Book [{book_id}] must be a positive integer")

    book_logger.log_info(f'Update Book id [{book_id}] price to {new_price}')

    old_price = book["price"]
    book["price"] = new_price

    book_logger.log_debug(f'Book [{book['title']}] price change: {old_price} --> {new_price}')
    request_logger.log_debug(str(time.perf_counter() - start))

    return ok(old_price)


#delete a book from database
@app.route("/book", methods=["DELETE"])
def delete_book():
    #log
    request_logger.log_info('DELETE')
    start = time.perf_counter()

    try:
        book_id = int(request.args.get("id"))
    except (TypeError, ValueError):
        return error(400, "Bad request: id must be numeric")

    if book_id not in books:
        return error(404, f"Error: no such book with id {book_id}")

    title = books[book_id]['title']
    book_logger.log_info(f'Removing book [{title}]')

    del books[book_id]

    book_logger.log_debug(f'After removing book [{title}] id: [{book_id}] there are {len(books)} books in the system')
    request_logger.log_debug(str(time.perf_counter() - start))

    # Return number of books REMAINING after deletion
    return ok(len(books))


#Gets the current level of a logger
@app.route("/logs/level", methods=["GET"])
def log_level():
    #log
    request_logger.log_info(method="GET")
    start = time.perf_counter()

    log_name = request.args.get('logger-name')
    if not log_name:
        return error(400, 'server error')
    if log_name not in loggers_dict.keys():
        return error(400, 'logger does not exist')


    logger_level = loggers_dict[log_name].level()
    level = 'DEBUG' if logger_level == 10 else 'INFO' if logger_level == 20 else 'ERROR' if logger_level == 30 else None
    if level is None:
        return error(400, 'level is unidentified')

    request_logger.log_debug(str(time.perf_counter() - start))
    return ok(level)

#sets the level of a logger
@app.route("/logs/level", methods=["PUT"])
def set_log_level():
    #log
    request_logger.log_info(method="PUT")
    start = time.perf_counter()

    data = request.json
    log_name = data.get('logger-name')
    req_level = data.get('logger-level')

    if not log_name:
        return error(400, 'server error')
    if log_name not in loggers_dict.keys():
        return error(400, 'logger does not exist')

    if req_level.upper() == 'ERROR':
        loggers_dict[log_name].set_level(logging.ERROR)
    elif req_level.upper() == 'INFO':
        loggers_dict[log_name].set_level(logging.INFO)
    elif req_level.upper() == 'DEBUG':
        loggers_dict[log_name].set_level(logging.DEBUG)
    else:
        return error(400, 'Cannot set logger level')

    request_logger.log_debug(str(time.perf_counter() - start))
    return ok('level changes successfully')

#server setup
if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8574, debug=False, use_reloader=False)

