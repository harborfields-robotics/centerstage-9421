def lerp(t, a, b):
	return a + t * (b - a)

def ilerp(x, a, b):
	return (x - a) / (b - a)

def remap(x, a0, b0, a1, b1):
	return lerp(ilerp(x, a0, b0), a1, b1)

print('point 0.725L 0.139R')
print('point 0.000L 0.936R')
print(f'interp point 0.072L -> {remap(0.072, 0.725, 0, 0.139, 0.936)} (real 0.757)')
