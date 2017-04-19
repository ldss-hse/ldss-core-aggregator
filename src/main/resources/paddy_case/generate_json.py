import os
import re

scales = {
    "9": [
        "w",
        "vp",
        "p",
        "mp",
        "m",
        "mg",
        "g",
        "vg",
        "a"
    ],
    "5": [
        "vp",
        "p",
        "m",
        "g",
        "vg"
    ],
    "7": [
        "vp",
        "p",
        "mp",
        "m",
        "mg",
        "g",
        "vg"
    ],
    "3": [
        "p",
        "m",
        "g"
    ]
}

criteria = {
    "МУА": ["К.МУА.1", "К.МУА.2", "К.МУА.3", "К.МУА.4", "К.МУА.5", "К.МУА.6"],
    "ЭКУА": ["К.ЭКУА.1", "К.ЭКУА.2", "К.ЭКУА.3", "К.ЭКУА.4", "К.ЭКУА.5", "К.ЭКУА.6"],
    "НУА": ["К.НУА.1", "К.НУА.2"],
    "ЮУА": ["К.ЮУА.1", "К.ЮУА.2"],
    "ПУА": ["К.ПУА.1", "К.ПУА.2", "К.ПУА.3"],
    "ЭПУА": ["К.ЭПУА.1"],
    "ЭТУА": ["К.ЭТУА.1", "К.ЭТУА.2"],
    "ЭСТУА": ["К.ЭСТУА.1", "К.ЭСТУА.2", "К.ЭСТУА.3"]
}

scalesID = {
    "3": "Scale_Three",
    "5": "Scale_Five",
    "7": "Scale_Seven",
    "9": "Scale_Nine"
}


def all_est_to_json(all_est_list):
    return '"estimations":{{ {} }}'.format(','.join(all_est_list))


def expert_est_to_json(exp_name, exp_est_list):
    exp_list_str = "[{}]".format(",".join(exp_est_list))
    return '"{}":{}'.format(exp_name,exp_list_str)


def alternative_est_to_json(alt_name, alt_est_list):
    alt_list_str = "[{}]".format(",".join(alt_est_list))
    return '{{"alternativeID":"{}", "criteria2Estimation":{}}}'.format(alt_name,
                                                                       alt_list_str)


def estimate_to_json(criteria_name, estimation):
    scale_str = ""
    if estimation['qualitative']:
        qualitative = "true"
        scale_id = scalesID[estimation['scaleSize']]
        scale_str = '"scaleID":"{}",'.format(scale_id)
    else:
        qualitative = "false"
    return '{{"criteriaID":"{}", "estimation":["{}"], {} "qualitative":{}}}'.format(criteria_name,
                                                                                      estimation['value'],
                                                                                      scale_str,
                                                                                     qualitative)

def read_expert_estimations(file_name):
    final_dic = {}
    with open('/Users/demidovs/Documents/Projects/lingvo-dss/src/main/resources/paddy_case/plain_results/ecolog.txt') as f:
        current_level = ""
        for line in f.readlines():
            if re.match('(МУА|ЭКУА|НУА|ЮУА|ПУА|ЭПУА|ЭТУА|ЭСТУА)', line):
                current_level = line.strip()
                print(current_level)
            elif re.match('А.(МУА|ЭКУА|НУА|ЮУА|ПУА|ЭПУА|ЭТУА|ЭСТУА).\d+\s*\&.*', line):
                # 1. find alt name
                alt_name = line.split('&')[0].strip()
                try:
                    final_dic[alt_name]
                except KeyError:
                    final_dic[alt_name] = {}
                for (i, est) in enumerate([i.strip() for i in line.split('&')[1:]]):
                    try:
                        tmp = int(str(est).replace("\\\\ \\hline", ""))
                        isQualitative = False
                    except ValueError:
                        isQualitative = True
                        numbers = re.findall('\d+', est)
                        label_position = int(numbers[0]) - 1
                        scale_size = numbers[1]
                        tmp = scales[str(scale_size)][label_position]
                    final_dic[alt_name][criteria[current_level][i]] = {"qualitative": isQualitative,
                                                                               "value": tmp, "scaleSize": scale_size}
    return final_dic

RESULTS_DIR = 'plain_results'

all_estimations_dic = {}
for subdir, dirs, files in os.walk(os.path.join(os.getcwd(), RESULTS_DIR)):
    for file in files:
        all_estimations_dic[file.split('.')[0]] = read_expert_estimations(os.path.join(subdir, file))

all_est = []
for (agent_name, agent_data) in all_estimations_dic.items():
    expert_est = []
    for (alt_name, alt_data) in agent_data.items():
        alternative_est = []
        for (criteria_name, criteria_data) in alt_data.items():
            alternative_est.append(estimate_to_json(criteria_name, criteria_data))
        expert_est.append(alternative_est_to_json(alt_name, alternative_est))
    all_est.append(expert_est_to_json(agent_name, expert_est))
print(all_est_to_json(all_est))
