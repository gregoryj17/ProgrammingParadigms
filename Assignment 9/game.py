import pygame
import time

from pygame.locals import*
from time import sleep

class Mario():
	def __init__(self, model):
		self.x=200
		self.y=0
		self.w=60
		self.h=95
		self.m=model
		self.vert_vel = 0
		self.frame = 0
		self.pic = 0
		self.moving = False
		self.toRemove = False
		self.lastGrounded = 0
	
	def update(self):
		self.vert_vel += 1.2
		self.prevY=self.y
		self.y += self.vert_vel
		if self.y > 500:
			self.vert_vel = 0.0
			self.y = 500 #snap back to the ground
			self.lastGrounded=self.frame
		self.prevX=self.x
		self.x=self.m.scrollPos+200

		for s in self.m.sprites:
			if self.collidesWith(s):
				if isinstance(s, Tube):
					self.getOutOfTube(s)
				elif isinstance(s, Goomba):
					self.enemyBounce(s)
		self.frame+=1
		if self.moving:
			self.pic=(self.pic+1)%5;

	def collidesWith(self, s):
		if s is self:
			return False
		if self.x+self.w<s.x:
			return False
		if self.y+self.h<s.y:
			return False
		if s.x+s.w<self.x:
			return False
		if s.y+s.h<self.y:
			return False
		#print "collision: " +str(self) + str(s)
		return True
			
	def getOutOfTube(self, s):
		if self.prevX+self.w<s.x and self.x+self.w>=s.x:
			self.x=s.x-self.w-1
			self.m.scrollPos=self.x-200
		elif self.prevX>s.x+s.w and self.x<=s.x+s.w:
			self.x=s.x+s.w+1
			self.m.scrollPos=self.x-200

		if self.prevY+self.h<s.y and self.y+self.h>=s.y:
			self.y=s.y-self.h-1
			self.vert_vel=0
			self.lastGrounded=self.frame
		elif self.prevY>s.y+s.h and self.y<=s.y+s.h:
			self.y=s.y+s.h+1
			self.vert_vel=0
	
	def enemyBounce(self, s):
		if self.prevX+self.w<s.x and self.x+self.w>=s.x:
			self.x=s.x-self.w-1
			self.m.scrollPos=self.x-200
		elif self.prevX>s.x+s.w and self.x<=s.x+s.w:
			self.x=s.x+s.w+1
			self.m.scrollPos=x-200
			
		if self.prevY+self.h<s.y and self.y+self.h>=s.y:
			self.y=s.y-self.h-1
			self.vert_vel=-20
			self.lastGrounded=self.frame-4
			s.toRemove=True
		elif self.prevY>s.y+s.h and self.y<=s.y+s.h:
			self.y=s.y+s.h+1
			self.vert_vel=0
			
	def jump(self):
		if self.frame-self.lastGrounded<=5:
			self.vert_vel -= 5
			
	def isMoving(self, left, right):
		self.moving = ((left^right) or self.vert_vel!=0)
		
	def getImage(self):
		return pygame.image.load("mario"+str(self.pic)+".png")
		
class Goomba():
	def __init__(self,x,y,m):
		self.x=x;
		self.y=y;
		self.right=True;
		self.turnSteps=300;
		self.curSteps=0;
		self.w=99;
		self.h=118;
		self.stepSize=1
		self.prevX=x
		self.frame=0
		self.burnedframe=-1
		self.m=m
		self.toRemove=False
	
	def update(self):
		self.prevX=self.x
		self.x+=self.stepSize*(1 if self.right else -1)
		self.curSteps+=1
		if self.curSteps>=self.turnSteps:
			self.curSteps=0
			self.right= not self.right
		for s in self.m.sprites:
			if self.collidesWith(s):
				if isinstance(s, Tube) or isinstance(s, Mario):
					self.getOutOfObject(s)
				elif isinstance(s, Fireball):
					if self.burnedframe==-1:
						self.burnedframe=self.frame
					s.toRemove=True
		if self.burnedframe!=-1 and self.frame>=self.burnedframe+10:
			self.toRemove=True
		self.frame+=1
		
	def getImage(self):
		return pygame.image.load("goomba.png" if self.burnedframe==-1 else "goomba_fire.png")
		
	def collidesWith(self, s):
		if s is self:
			return False
		if self.x+self.w<s.x:
			return False
		if self.y+self.h<s.y:
			return False
		if s.x+s.w<self.x:
			return False
		if s.y+s.h<self.y:
			return False
		#print "collision: " +str(self) + str(s)
		return True
		
	def getOutOfObject(self, s):
		if self.prevX+self.w<s.x and self.x+self.w>=s.x:
			self.x=s.x-self.w-1
		elif self.prevX>s.x+s.w and self.x<=s.x+s.w:
			self.x=s.x+s.w+1
		self.curSteps=0
		self.right= not self.right
	
class Fireball():
	def __init__(self,x,y,m):
		self.x=x
		self.y=y
		self.w=47
		self.h=47
		self.vert_vel=0
		self.horiz_vel=4
		self.prevX=x
		self.prevY=y
		self.toRemove=False
		self.m=m
		
	def update(self):
		self.vert_vel += 1.2
		self.prevY=self.y
		self.y += self.vert_vel
		if self.y > 548:
			self.vert_vel-=0.5
			self.vert_vel *= -1.0
			self.y = 548 #snap back to the ground
		self.prevX=self.x
		self.x+=self.horiz_vel
		if self.x-self.m.scrollPos>1600 or self.x-self.m.scrollPos<-50:
			self.toRemove=True
		for s in self.m.sprites:
			if self.collidesWith(s):
				if isinstance(s, Tube):
					self.getOutOfObject(s)
					
	def collidesWith(self, s):
		if s is self:
			return False
		if self.x+self.w<s.x:
			return False
		if self.y+self.h<s.y:
			return False
		if s.x+s.w<self.x:
			return False
		if s.y+s.h<self.y:
			return False
		#print "collision: " +str(self) + str(s)
		return True
		
	def getOutOfObject(self, s):
		if self.prevX+self.w<s.x and self.x+self.w>=s.x:
			self.x=s.x-self.w-1
			self.horiz_vel*=-1
		elif self.prevX>s.x+s.w and self.x<=s.x+s.w:
			self.x=s.x+s.w+1
			self.horiz_vel*=-1

		if self.prevY+self.h<s.y and self.y+self.h>=s.y:
			self.y=s.y-self.h-1
			self.vert_vel*=-1
		elif self.prevY>s.y+s.h and self.y<=s.y+s.h:
			self.y=s.y+s.h+1
			self.vert_vel*=-1
			
	def getImage(self):
		return pygame.image.load("fireball.png")
	
	
class Tube():
	def __init__(self,x,y,m):
		self.x=x
		self.y=y
		self.w=55
		self.h=400
		self.m=m
		self.toRemove=False
		
	def update(self):
		self.x=self.x
	
	def getImage(self):
		return pygame.image.load("tube.png")
	
	
class Model():
	def __init__(self):
		self.sprites = []
		self.mario = Mario(self)
		self.sprites.append(self.mario)
		self.scrollPos=0
		self.start()

	def shootFlame(self):
		f = Fireball(self.mario.x,self.mario.y,self)
		self.sprites.append(f)
	
	def scroll(self, scrollAmount):
		self.scrollPos += scrollAmount
		
	def update(self):
		for s in self.sprites:
			s.update()
		
		for i in range(len(self.sprites)):
			if i<len(self.sprites) and self.sprites[i].toRemove:
				self.sprites.pop(i)
				i-=1
				
	def start(self):
		self.sprites.append(Tube(557,496,self))
		self.sprites.append(Tube(875,437,self))
		self.sprites.append(Goomba(766,478,self))

class View():
	def __init__(self, model):
		screen_size = (800,700)
		self.screen = pygame.display.set_mode(screen_size, 32)
		self.turtle_image = pygame.image.load("turtle.png")
		self.model = model
		self.model.rect = self.turtle_image.get_rect()

	def update(self):    
		self.screen.fill([0,200,100])
		self.screen.fill([128,128,128],Rect(0, 596, 2000, 1))
		for s in self.model.sprites:
			self.screen.blit(s.getImage(), Rect(s.x-self.model.scrollPos,s.y,s.w,s.h))
		pygame.display.flip()

class Controller():
	def __init__(self, model):
		self.model = model
		self.keep_going = True

	def update(self):
		for event in pygame.event.get():
			if event.type == QUIT:
				self.keep_going = False
			elif event.type == KEYDOWN:
				if event.key == K_ESCAPE:
					self.keep_going = False
			#elif event.type == pygame.MOUSEBUTTONUP:
				#self.model.set_dest(pygame.mouse.get_pos())
		keys = pygame.key.get_pressed()
		keyLeft=False
		keyRight=False
		if keys[K_LEFT]:
			self.model.scroll(-4)
			keyLeft = True
		if keys[K_RIGHT]:
			self.model.scroll(4)
			keyRight = True
		if keys[K_UP]:
			self.model.mario.jump()
		#if keys[K_DOWN]:
			#print "you pressed down"
		if keys[K_LCTRL]:
			self.model.shootFlame()
		self.model.mario.isMoving(keyLeft,keyRight)
		keyLeft=False
		keyRight=False
			
print("Use the arrow keys to move. Use control to shoot a fireball. Press Esc to quit.")
pygame.init()
m = Model()
v = View(m)
c = Controller(m)
while c.keep_going:
	c.update()
	m.update()
	v.update()
	sleep(0.04)
print("Goodbye")