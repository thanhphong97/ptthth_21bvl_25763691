package bai9;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Auction extends UnicastRemoteObject implements IAuction {

    private final LinkedHashMap<String, Product> products = new LinkedHashMap<>();
    private final List<IAuctionClient> clients = new CopyOnWriteArrayList<>();

    public Auction() throws RemoteException {
        super();
    }

    @Override
    public synchronized boolean addProduct(String seller, String productName, double startPrice)
            throws RemoteException {
        if (products.containsKey(productName) || startPrice <= 0) {
            System.out.println(seller + " thêm sản phẩm " + productName + " thất bại");
            return false;
        }
        products.put(productName, new Product(productName, seller, startPrice));
        System.out.println(seller + " thêm sản phẩm " + productName + " giá khởi điểm " + startPrice);
        return true;
    }

    @Override
    public synchronized List<Product> getProducts() throws RemoteException {
        return new ArrayList<>(products.values());
    }

    @Override
    public boolean placeBid(String bidder, String productName, double amount) throws RemoteException {
        synchronized (this) {
            Product product = products.get(productName);
            if (product == null || amount <= product.getCurrentPrice()) {
                System.out.println(bidder + " đặt giá " + amount + " cho " + productName + " thất bại");
                return false;
            }
            product.setBid(bidder, amount);
        }
        String message = bidder + " vừa đặt giá cao hơn cho " + productName + ": " + amount;
        System.out.println(message);
        notifyClients(message);
        return true;
    }

    @Override
    public void registerClient(IAuctionClient client) throws RemoteException {
        clients.add(client);
        System.out.println("Có client mới kết nối, tổng số: " + clients.size());
    }

    @Override
    public void unregisterClient(IAuctionClient client) throws RemoteException {
        clients.remove(client);
        System.out.println("Một client đã thoát, tổng số: " + clients.size());
    }

    private void notifyClients(String message) {
        for (IAuctionClient client : clients) {
            try {
                client.receiveNotification(message);
            } catch (RemoteException e) {
                clients.remove(client);
            }
        }
    }
}
