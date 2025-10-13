package ru.ssau.tk.pepper.oopopopop.io;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import ru.ssau.tk.pepper.oopopopop.functions.ArrayTabulatedFunction;
import ru.ssau.tk.pepper.oopopopop.functions.Point;
import ru.ssau.tk.pepper.oopopopop.functions.TabulatedFunction;
import ru.ssau.tk.pepper.oopopopop.functions.factory.TabulatedFunctionFactory;

import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;


public final class FunctionsIO {
    private FunctionsIO() {
        throw new UnsupportedOperationException();
    }

    public static void writeTabulatedFunction(BufferedWriter writer, TabulatedFunction function) {
        PrintWriter printWriter = new PrintWriter(writer);
        printWriter.printf("%d\n", function.getCount());
        for (Point p: function) {
            printWriter.printf("%f %f\n", p.x, p.y);
        }
        printWriter.flush();
    }

    public static void writeTabulatedFunction(BufferedOutputStream outputStream, TabulatedFunction function) throws IOException {
        DataOutputStream stream = new DataOutputStream(outputStream);
        stream.writeInt(function.getCount());
        for (Point p: function) {
            stream.writeDouble(p.x);
            stream.writeDouble(p.y);
        }
        stream.flush();
    }

    public static TabulatedFunction readTabulatedFunction(BufferedReader reader, TabulatedFunctionFactory factory) throws IOException {
        try {
            int count = Integer.parseInt(reader.readLine());
            double[] xValues = new double[count];
            double[] yValues = new double[count];
            NumberFormat format = NumberFormat.getInstance(Locale.forLanguageTag("ru"));
            for (int i = 0; i < count; ++i) {
                String[] xy = reader.readLine().split("\\s");
                if (xy.length != 2) {
                    throw new IOException("Invalid file format.");
                }
                xValues[i] = format.parse(xy[0]).doubleValue();
                yValues[i] = format.parse(xy[1]).doubleValue();
            }
            return factory.create(xValues, yValues);
        } catch (NumberFormatException | ParseException e) {
            throw new IOException(e.getMessage());
        }
    }

    public static TabulatedFunction readTabulatedFunction(BufferedInputStream inputStream, TabulatedFunctionFactory factory) throws IOException {
        DataInputStream stream = new DataInputStream(inputStream);
        int count = stream.readInt();
        double[] xValues = new double[count];
        double[] yValues = new double[count];
        for (int i = 0; i < count; ++i) {
            xValues[i] = stream.readDouble();
            yValues[i] = stream.readDouble();
        }
        return factory.create(xValues, yValues);
    }

    public static void serialize(BufferedOutputStream stream, TabulatedFunction function) throws IOException {
        new ObjectOutputStream(stream).writeObject(function);
        stream.flush();
    }

    public static TabulatedFunction deserialize(BufferedInputStream stream) throws IOException, ClassNotFoundException {
        return (TabulatedFunction) new ObjectInputStream(stream).readObject();
    }

    public static void serializeXml(BufferedWriter writer, TabulatedFunction function) throws IOException {
        XStream xStream = new XStream();
        xStream.toXML(function, writer);
        writer.flush();
    }

    // Сделаем метод параметризованным, чтобы можно было использовать с обеими реализациями.
    @SuppressWarnings("unchecked")
    public static <T extends TabulatedFunction> T deserializeXml(BufferedReader reader) {
        XStream xStream = new XStream();
        xStream.addPermission(AnyTypePermission.ANY);
        return (T) xStream.fromXML(reader);
    }
}
