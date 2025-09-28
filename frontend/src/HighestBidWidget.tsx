import { useEffect, useState } from "react";

import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

type Props = { auctionId: number };

const HighestBidWidget: React.FC<Props> = ({ auctionId }) => {
    const [highestBid, setHighestBid] = useState<number | null>(null);

    useEffect(() => {
        const socket = new SockJS("http://localhost:8080/ws");
        const client = new Client({
            webSocketFactory: () => socket as any,
            reconnectDelay: 4000,
            onConnect: () => {
                client.subscribe(`/topic/auctions/${auctionId}/highest`, (msg) => {
                    if (msg.body) {
                        const data = JSON.parse(msg.body);
                        if (data.amount) setHighestBid(data.amount);
                    }
                });
            },
        });

        client.activate();

        // ✅ cleanup fixed
        return () => {
            client.deactivate();
        };
    }, [auctionId]);

    return (
        <div>
            <h3>Highest Bid</h3>
            <p>{highestBid !== null ? `${highestBid} LKR` : "No bids yet"}</p>
        </div>
    );
};

export default HighestBidWidget;
