package net.idik.lib.cipher.so.extension

class KeyExt {
    String name
    String value

    public KeyExt(String name) {
        this.name = name
    }

    @Override
    public String toString() {
        return "KeyExt{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}