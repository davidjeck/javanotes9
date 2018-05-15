
// Null pointer exception in Eclipse.

public class HelloWorldConsole {
	
	public static void main(String[] args) {
		String name;
		name = System.console().readLine("Hi, what's your name? ");
		System.console().printf("Hello, %s%n", name);
	}

}
