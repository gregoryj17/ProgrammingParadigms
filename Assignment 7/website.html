<head>
	<title>Turtle Attack!</title>
	<meta charset="UTF-8">
</head>
<body>
<br>
<canvas id="myCanvas" width="1000" height="500" style="border:1px solid #cccccc;"></canvas>

<script type="text/javascript">

function Sprite(x, y, image_url, update_method, onclick_method)
{
	this.x = x;
	this.y = y;
	this.image = new Image();
	this.image.src = image_url;
	this.update = update_method;
	this.onclick = onclick_method;
}

Sprite.prototype.set_destination = function(x, y)
{
	this.dest_x = x;
	this.dest_y = y;
}

Sprite.prototype.ignore_click = function(x, y)
{
}

Sprite.prototype.move = function(dx, dy)
{
	this.dest_x = this.x + dx;
	this.dest_y = this.y + dy;
}

Sprite.prototype.go_toward_destination = function()
{
	if(this.dest_x === undefined)
		return;

	if(this.x < this.dest_x)
		this.x++;
	else if(this.x > this.dest_x)
		this.x--;
	if(this.y < this.dest_y)
		this.y++;
	else if(this.y > this.dest_y)
		this.y--;
}

Sprite.prototype.sit_still = function()
{
}







function Model()
{
	this.sprites = [];
	this.sprites.push(new Sprite(200, 100, "lettuce.png", Sprite.prototype.sit_still, Sprite.prototype.ignore_click));
	this.turtle = new Sprite(50, 50, "turtle.png", Sprite.prototype.go_toward_destination, Sprite.prototype.set_destination);
	this.sprites.push(this.turtle);
}

Model.prototype.update = function()
{
	for(let i = 0; i < this.sprites.length; i++)
	{
		this.sprites[i].update();
	}
}

Model.prototype.onclick = function(x, y)
{
	for(let i = 0; i < this.sprites.length; i++)
	{
		this.sprites[i].onclick(x, y);
	}
}

Model.prototype.move = function(dx, dy)
{
	this.turtle.move(dx, dy);
}






function View(model)
{
	this.model = model;
	this.canvas = document.getElementById("myCanvas");
	this.turtle = new Image();
	this.turtle.src = "turtle.png";
}

View.prototype.update = function()
{
	let ctx = this.canvas.getContext("2d");
	ctx.clearRect(0, 0, 1000, 500);
	for(let i = 0; i < this.model.sprites.length; i++)
	{
		let sprite = this.model.sprites[i];
		ctx.drawImage(sprite.image, sprite.x, sprite.y);
	}
}









function Controller(model, view)
{
	this.model = model;
	this.view = view;
	this.key_right = false;
	this.key_left = false;
	this.key_up = false;
	this.key_down = false;
	let self = this;
	view.canvas.addEventListener("click", function(event) { self.onClick(event); });
	document.addEventListener('keydown', function(event) { self.keyDown(event); }, false);
	document.addEventListener('keyup', function(event) { self.keyUp(event); }, false);
}

Controller.prototype.onClick = function(event)
{
	this.model.onclick(event.pageX - this.view.canvas.offsetLeft, event.pageY - this.view.canvas.offsetTop);
}

Controller.prototype.keyDown = function(event)
{
	if(event.keyCode == 39) this.key_right = true;
	else if(event.keyCode == 37) this.key_left = true;
	else if(event.keyCode == 38) this.key_up = true;
	else if(event.keyCode == 40) this.key_down = true;
}

Controller.prototype.keyDown = function(event)
{
	if(event.keyCode == 39) this.key_right = true;
	else if(event.keyCode == 37) this.key_left = true;
	else if(event.keyCode == 38) this.key_up = true;
	else if(event.keyCode == 40) this.key_down = true;
}

Controller.prototype.keyUp = function(event)
{
	if(event.keyCode == 39) this.key_right = false;
	else if(event.keyCode == 37) this.key_left = false;
	else if(event.keyCode == 38) this.key_up = false;
	else if(event.keyCode == 40) this.key_down = false;
}

Controller.prototype.update = function()
{
	let dx = 0;
	let dy = 0;
	if(this.key_right) dx++;
	if(this.key_left) dx--;
	if(this.key_up) dy--;
	if(this.key_down) dy++;
	if(dx != 0 || dy != 0)
		this.model.move(dx, dy);
}







function Game()
{
	this.model = new Model();
	this.view = new View(this.model);
	this.controller = new Controller(this.model, this.view);
}

Game.prototype.onTimer = function()
{
	this.controller.update();
	this.model.update();
	this.view.update();
}



let game = new Game();
let timer = setInterval(function() { game.onTimer(); }, 40);

</script>

</body>
