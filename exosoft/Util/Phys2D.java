package exosoft.Util;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Phys2D {
	@Deprecated
	public Rectangle[] createGrid(BufferedImage bitmap) {
		List<Rectangle> shapeList = new ArrayList<Rectangle>();
		for (int x = 0; x < bitmap.getWidth(); x++) {
			for (int y = 0; y < bitmap.getHeight(); y++) {
				if (bitmap.getRGB(x, y) == 0x000000) {
					shapeList.add(new Rectangle(x * 10, y * 10, 10, 10));
				}
			}
		}
		Rectangle[] shapeArray = new Rectangle[shapeList.size()];
		for (int i = 0; i < shapeList.size(); i++){
			shapeArray[i] = shapeList.get(i);
		}
		return shapeArray;
	}

	public Shape createMesh(BufferedImage bitmap) {
		// TODO Auto-generated method stub
		return null;
	}

	public Shape[] createMeshes(BufferedImage bitmap) {
		// TODO Auto-generated method stub
		return null;
	}
}
