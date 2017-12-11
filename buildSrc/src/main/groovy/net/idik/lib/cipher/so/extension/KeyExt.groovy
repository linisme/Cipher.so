package net.idik.lib.cipher.so.extension

class KeyExt implements Serializable {

    private static final long serialVersionUID = -4944583644545092938L

    String name
    String value

    KeyExt(String name) {
        this.name = name
    }

    @Override
    String toString() {
        return "KeyExt{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}