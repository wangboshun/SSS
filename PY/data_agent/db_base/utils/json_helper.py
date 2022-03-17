import json
import os
import jsonpath


class json_helper:
    json_config = None

    @classmethod
    def __load_json__(cls, file_path=None):
        path = file_path
        if path is None:
            parent = os.path.dirname(os.path.realpath(__file__))
            parent = os.path.dirname(parent)
            parent = os.path.dirname(parent)
            path = os.path.join(parent, "app_config.json")

        if not os.path.exists(path):
            with open(path, 'w') as json_file:
                pass
        with open(path) as json_file:
            try:
                cls.json_config = json.load(json_file)
            except:
                cls.json_config = {}

    @classmethod
    def get_val(cls, key: str, file_path=None):
        if cls.json_config is None:
            cls.__load_json__()

        array = key.split(':')
        path = "$"
        if len(array) > 0:
            for i in array:
                path = path + "." + i
        else:
            path = path + "." + key
        return jsonpath.jsonpath(cls.json_config, path)
