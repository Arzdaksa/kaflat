import logging
import time
from logging import DEBUG

from pyexpat.errors import messages


#-- request logger --
class RequestLogger:
    counter = 0
    def __init__(self):
        self.logger = logging.getLogger(name='request-logger')
        self.logger.setLevel(level=logging.INFO)
        self.handler = logging.FileHandler('requests.log', mode='w')
        self.self_logging()

    def log_info(self, method:str):
        info_format = logging.Formatter(f'incoming request | #{self.counter} | %(name)s | %(message)s')
        self.handler.setFormatter(info_format)
        self.logger.addHandler(self.handler)
        self.logger.info(method.upper())

    def log_debug(self, duration):
        debug_format = logging.Formatter(f'request #{self.counter} duration: %(message)s ms')
        self.handler.setFormatter(debug_format)
        self.logger.addHandler(self.handler)
        self.logger.debug(str(duration))
        self.counter += 1

    def log_error(self, message, code):
        error_format = logging.Formatter(f'{code} error, request #{self.counter}: %(message)s')
        self.handler.setFormatter(error_format)
        self.logger.addHandler(self.handler)
        self.logger.error(message)

    def self_logging(self):
        self_format = logging.Formatter('%(asctime)s | request logger init')
        self.handler.setFormatter(self_format)
        self.logger.addHandler(self.handler)
        self.logger.info('')

    def name(self):
        return self.logger.name

    def level(self):
        return self.logger.level

    def set_level(self, new_level):
        self.logger.setLevel(level=new_level)

class BookLogger:
    def __init__(self):
        self.logger = logging.getLogger('books-logger')
        self.logger.setLevel(level=logging.INFO)
        self.handler = logging.FileHandler('books.log', mode='w')
        self.logger.addHandler(self.handler)
        self.log_info('Book logger init')

    def log_info(self, message:str):
        self.logger.info(msg=message)

    def log_debug(self, message:str):
        self.logger.debug(msg=message)

    def name(self):
        return self.logger.name

    def level(self):
        return self.logger.level

    def set_level(self, new_level):
        self.logger.setLevel(level=new_level)