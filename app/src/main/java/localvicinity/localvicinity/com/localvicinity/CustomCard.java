package localvicinity.localvicinity.com.localvicinity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.view.View;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;

/**
 * Created by Tim on 3/2/2015.
 */

public class CustomCard extends Card {

    public CustomCard(Context context, final LocationType lt, int thumb, int color) {
        super(context);

        CardHeader cardheader;
        LocationTypeDescriptor ltd = new LocationTypeDescriptor();
        cardheader = new CardHeader(getContext());
        cardheader.setTitle(ltd.typeDescription(lt));
        CardThumbnail thumbnail = new CardThumbnail(getContext());
        thumbnail.setDrawableResource(thumb);
        this.setBackgroundResourceId(color);
        this.addCardHeader(cardheader);
        this.addCardThumbnail(thumbnail);
        this.setClickable(true);

        //Set onClick listener
        this.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Intent intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra("category", lt);
                getContext().startActivity(intent);
            }
        });

    }
}
