var entity = new andengine.Entity(10, 20);

entity.onAttached = function() {
	return true;
}

entity.x = entity.y * 2;