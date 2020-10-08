from random import Random


def generate(size):
    rnd = Random()
    name = str(size) + ".txt"
    f = open(name, "w+")
    f.write(str(size) + "\n")
    number1 = 0.0
    number2 = 0.0
    for i in range(size):
        number1 = rnd.uniform(0, 1)
        number2 = rnd.uniform(0, 1)
        f.write(str(number1) + " " + str(number2)+ "\n")
    f.close()


if __name__ == '__main__':
    generate(1000000)
    generate(2000000)
    generate(4000000)
    generate(8000000)