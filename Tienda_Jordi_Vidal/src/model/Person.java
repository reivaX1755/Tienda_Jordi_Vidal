package model;

public abstract class Person {
	
	protected String name;

    public Person(String name) {
        this.name = name;
    }

	protected String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

}
