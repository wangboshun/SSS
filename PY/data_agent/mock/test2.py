import turtle

turtle.setup(800, 800, 100, 100)
color = ['red', 'blue', 'yellow', 'purple']
for i in range(4):
    turtle.begin_fill()

    turtle.circle(50)
    turtle.color(color[i])
    turtle.end_fill()
    turtle.penup()
    turtle.goto(0, 80 * (i + 1))
    turtle.pendown()
done()
