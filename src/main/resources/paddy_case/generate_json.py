import re

final_dic = {}
with open('/Users/demidovs/Documents/Projects/lingvo-dss/src/main/resources/paddy_case/plain_results/ecolog.txt') as f:
    for line in f.readlines():
        if re.match('(МУА|ЭКУА|НУА|ЮУА|ПУА|ЭПУА|ЭТУА|ЭСТУА)', line):
            final_dic[line.strip()]  = []
        elif re.match('А.(МУА|ЭКУА|НУА|ЮУА|ПУА|ЭПУА|ЭТУА|ЭСТУА).\d+\s*\&.*', line):
            for est in [i.strip() for i in line.split('&')[1:]]:
                try:
                    tmp = int(est)
                except ValueError:
                    tmp = re.findall('s_\{\d+\}\^\{\d+\}', est)
                    numbers = re.findall('\d+', est)
                    label_position = numbers[0]
                    scale_size = numbers[0]


