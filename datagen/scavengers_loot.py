import csv
import json
import os
import shutil
from copy import deepcopy


def assert_dir(path):
    if not os.path.exists(path):
        os.makedirs(path)


def assert_not_dir(path):
    if os.path.exists(path):
        shutil.rmtree(path)


def write_loot_tables(loot_dir, data, biome):
    with open(loot_dir + 'minecraft/' + biome + '.json', 'w') as out_file:
        json.dump(data, out_file, indent=4, sort_keys=True)


def main():
    dir_pack = './out/pollinators_paradise'
    dir_loot = dir_pack + '/data/pollinators_paradise/loot_tables/'
    assert_not_dir(dir_pack)
    shutil.copytree('override', dir_pack)
    assert_dir(dir_loot)
    assert_dir(dir_loot + 'minecraft/')

    with open('./template/loot.json', 'r') as template_file:
        template = json.load(template_file)

    with open('Nest_Scavengers_Loot_Tables.csv', 'r') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            loot = deepcopy(template)
            for index, weight in enumerate([120, 60, 10, 6, 2, 1]):
                stack = row[str(weight)]
                count, item = stack.split('x ')
                loot['pools'][0]['entries'][index]['name'] = item
                loot['pools'][0]['entries'][index]['functions'][0]['count'] = int(count)
            write_loot_tables(dir_loot, loot, row['biome'])
        return


if __name__ == "__main__":
    main()
