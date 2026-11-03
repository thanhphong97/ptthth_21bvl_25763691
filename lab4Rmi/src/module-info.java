module lab4Rmi {
	// java.rmi is a separate JDK module; a named module only reads java.base by default
	requires java.rmi;

	// RMI gọi remote interface bằng reflection nên cần exports cho java.rmi
	exports bai1 to java.rmi;
	exports bai2 to java.rmi;
	exports bai3 to java.rmi;
	exports bai4 to java.rmi;
	exports bai5 to java.rmi;
	exports bai6 to java.rmi;
	exports bai7 to java.rmi;
	exports bai8 to java.rmi;
	exports bai9 to java.rmi;
}
