package br.com.v8developmentstudio.rccguarulhos.calendar.activitys;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.List;

import br.com.v8developmentstudio.rccguarulhos.calendar.R;
import br.com.v8developmentstudio.rccguarulhos.calendar.adapter.MyRecyclerScroll;
import br.com.v8developmentstudio.rccguarulhos.calendar.adapter.ScaleImageView;
import br.com.v8developmentstudio.rccguarulhos.calendar.dao.PersistenceDao;
import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.Calendario;
import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.Evento;
import br.com.v8developmentstudio.rccguarulhos.calendar.modelo.EventoFavorito;
import br.com.v8developmentstudio.rccguarulhos.calendar.permissions.PermissionService;
import br.com.v8developmentstudio.rccguarulhos.calendar.services.ActivityServices;
import br.com.v8developmentstudio.rccguarulhos.calendar.services.ActivityServicesImpl;
import br.com.v8developmentstudio.rccguarulhos.calendar.services.CalendarEventService;
import br.com.v8developmentstudio.rccguarulhos.calendar.task.DownloadImagesTask;
import br.com.v8developmentstudio.rccguarulhos.calendar.util.Constantes;

import android.widget.Toast;

/**
 * Created by cleiton.dantas on 18/03/2016.
 */
public class DescricaoActivity extends AppCompatActivity {

    private PersistenceDao persistenceDao = PersistenceDao.getInstance(this);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy \n HH:mm");
    private Evento evento;
    private Calendario calendario;
    private List<EventoFavorito> eventoFavoritos;
    private int activityHistory;
    private Toolbar toolbar;
    private ActivityServices ac = new ActivityServicesImpl();
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private CalendarEventService calendarEventService;
    private FloatingActionButton fabMenu, fabShare,fabAddCalendar,fabMaps;
    private TextView textViewSumario,textViewDescricao,textViewDataHoraInicio,textViewDataHoraFim,textViewLocal;
    private ScaleImageView thumbnail;
    private Button buttonLink;
    public static final String TAG = "LOG";
    public static final int REQUEST_PERMISSIONS_CODE = 128;
    public boolean controler;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private  PermissionService permissionService;
    private int currentMonthIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.descricao_cards_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        permissionService = new PermissionService(this,this);
        persistenceDao = PersistenceDao.getInstance(this);
        currentMonthIndex = getIntent().getIntExtra(Constantes.CURRENT_MONTH,0);
        int id = getIntent().getIntExtra(Constantes.ID, 1);
        activityHistory = getIntent().getIntExtra(Constantes.ACTIVITYHISTOTY, 0);

        evento=(Evento) getIntent().getSerializableExtra(Constantes.OBJ_EVENTO);
        if(evento==null){
            String uid =  getIntent().getStringExtra(Constantes.UID);
            if(uid!=null && uid.length()>0){
                List<Evento> events = persistenceDao.recuperaEventoPorUID(uid, persistenceDao.openDB());
                for(Evento item : events){
                    evento = item;
                }
            }
        }
        if(evento==null){
            evento = persistenceDao.recuperaEventoPorID(id,persistenceDao.openDB(this));
        }
        calendario = persistenceDao.recuperaConfigCalendarPorID(evento.getIdCalendario(),persistenceDao.openDB(this));

        toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calendarEventService = new CalendarEventService(this);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT); // transperent color = #00000000
        TableRow tbRow = (TableRow) findViewById(R.id.idTbButtonLink);
        buttonLink = (Button) findViewById(R.id.buttonlink);
        textViewSumario = (TextView) findViewById(R.id.idsumario);
        textViewDescricao = (TextView) findViewById(R.id.idDescricao);
        textViewDataHoraInicio = (TextView) findViewById(R.id.idDataHoraInicio);
        textViewDataHoraFim = (TextView) findViewById(R.id.idDataHoraFim);
        textViewLocal = (TextView) findViewById(R.id.idLocal);
        thumbnail = (ScaleImageView) findViewById(R.id.thumbnail);
        final ProgressBar viewProgressBar = (ProgressBar) findViewById(R.id.pbHeaderProgress);


        viewProgressBar.setVisibility(View.GONE);

        final Animation animeFloating = AnimationUtils.loadAnimation(this, R.anim.rotate);
        final Animation animeFloating2 = AnimationUtils.loadAnimation(this, R.anim.rotate2);

        fabMaps = (FloatingActionButton) findViewById(R.id.fabMaps);
        fabMenu = (FloatingActionButton) findViewById(R.id.idFabMenu);
        fabShare = (FloatingActionButton) findViewById(R.id.idFabShare);
        fabAddCalendar = (FloatingActionButton) findViewById(R.id.idFabAddCalendar);
        fabShare.hide();
        fabAddCalendar.hide();

        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

        fabMenu.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(View.GONE == fabShare.getVisibility()) {
                   fabMenu.startAnimation(animeFloating);
                   fabShare.show();
                   fabAddCalendar.show();
               }else{
                   fabMenu.startAnimation(animeFloating2);
                   fabShare.hide();
                   fabAddCalendar.hide();
               }
           }
       });

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareBody = prepereShare(evento);
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,evento.getSumario());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent,"RCC Share"));
            }
        });



        fabAddCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(permissionService.getPermission()){
                    addEventoLocalCalendar();
                }else{
                    mensagemAviso();
                }
            }
        });

        fabMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareLocation();
            }
        });


        Display disply = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        disply.getSize(size);

        if(evento.getUri()!=null){
            tbRow.setVisibility(View.VISIBLE);
        }else{
            tbRow.setVisibility(View.GONE);
        }

        if (evento.getUrlImg() != null) {
            viewProgressBar.setVisibility(View.VISIBLE);
            thumbnail.setTag(evento.getUrlImg());
            Object[] obj = {thumbnail, evento.getUrlImg(),viewProgressBar};
            new DownloadImagesTask().execute(obj);
            int width = size.x;
            int height = size.y/2;
            thumbnail.setMaxHeight(height);
            thumbnail.setMinimumHeight(height);
            thumbnail.setMaxWidth(width);
            thumbnail.setMinimumWidth(width);
            appBarLayout.setExpanded(true);
            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    zoomImageFromThumb(thumbnail,thumbnail.getDrawable());
                    viewProgressBar.setVisibility(View.GONE);
                }
            });
        }else{
            int width = size.x/9;
            int height = size.y/9;
            thumbnail.setMaxHeight(height);
            thumbnail.setMinimumHeight(height);
            thumbnail.setMaxWidth(width);
            thumbnail.setMinimumWidth(width);
            appBarLayout.setExpanded(false);
        }

        textViewSumario.setText(evento.getSumario());
        textViewDescricao.setText(evento.getDescricao());
        textViewDataHoraInicio.setText(dateFormat.format(evento.getDataHoraInicio()));
        textViewDataHoraFim.setText(dateFormat.format(evento.getDataHoraFim()));
        textViewLocal.setText(evento.getLocal());
        eventoFavoritos = persistenceDao.recuperaFavoritoPorUID(evento.getUid(),persistenceDao.openDB(this));


        buttonLink.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                onWevView();
            }
        });



    }
    @Override
    protected void onResume() {
        super.onResume();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    private void addEventoLocalCalendar(){
        calendarEventService.addEventoAoCalendarioLocal(evento);
        Toast.makeText(this,"EVENTO ADD NO CALENDARIO LOCAL", Toast.LENGTH_SHORT).show();

    }

    private void mensagemAviso(){
        String loremIpsum = "Para utilizar o recurso de salvar e evento na sua agenda é necessario que o usuário conseda as permissões de leitura de Agenda e Escrita em Calendario";

        AlertDialog.Builder builder = new AlertDialog.Builder(DescricaoActivity.this);
        builder.setTitle("Aviso");
        builder.setMessage(loremIpsum);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                permissionService.callGetPermissions();
            }
        });
        builder.show();
    }

    private void onWevView(){
        Bundle dados = new Bundle();
        dados.putString(Constantes.URI, evento.getUri());
        ac.redirect(this, WebViewActivity.class, dados);
    }

    private String prepereShare(Evento evento){
       final SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String msg ="";

        if(evento.getSumario()!=null){
            msg+=evento.getSumario()+"\n \n";
        }
        if(evento.getDescricao()!=null){
            msg+=evento.getDescricao()+"\n";
        }
        if(evento.getLocal()!=null){
            msg+="LOCAL: "+evento.getLocal()+"\n";
        }
        if(evento.getDataHoraInicio()!=null){
            msg+="INICIO: "+dateFormat2.format(evento.getDataHoraInicio())+"\n";
        }
        if(evento.getDataHoraFim()!=null){
            msg+="FIM: "+dateFormat2.format(evento.getDataHoraFim());
        }
        return msg;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem someMenuItem = menu.findItem(R.id.action_star);
        if (eventoFavoritos!=null && eventoFavoritos.size() > 0) {
            someMenuItem.setIcon(android.R.drawable.btn_star_big_on);
        } else {
            someMenuItem.setIcon(android.R.drawable.btn_star_big_off);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_star:
                pressFavorito(item);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Bundle dados = new Bundle();
        switch (activityHistory) {
            case 0:
                dados.putInt(Constantes.CURRENT_MONTH,currentMonthIndex);
                ac.redirect(this, MainActivity.class, dados);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case 1:
                ac.redirect(this, ListaEventosFavoritosActivity.class, null);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case 2:

                dados.putInt(Constantes.ID, calendario.getId());
                dados.putString(Constantes.CALENDARIO, calendario.getNomeLabel());
                ac.redirect(this, ListaEventosActivity.class, dados);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;

        }

    }
    private void showAllAnimateFab(FloatingActionButton... fabsArray){
        for (int i=0;i<fabsArray.length;i++){
            fabsArray[i].animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
        }
    }
    private void hideAllAnimateFab(FloatingActionButton... fabsArray){
        for (int i=0;i<fabsArray.length;i++){
            fabsArray[i].animate().translationY(fabMenu.getHeight() + 200).setInterpolator(new AccelerateInterpolator(2)).start();
        }
    }

    private boolean shareLocation(){
        if(evento.getLocal()!=null && !evento.getLocal().contains("A Definir")) {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + evento.getLocal());
            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package

            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            }else{
            Toast.makeText(this,"Não foi possivel abrir Google Maps", Toast.LENGTH_SHORT).show();
            }
        // Attempt to start an activity that can handle the Intent
            return true;
        }else{
            return false;
        }
    }


    private void pressFavorito(MenuItem item) {
        eventoFavoritos = persistenceDao.recuperaFavoritoPorUID(evento.getUid(),persistenceDao.openDB(this));
        if (eventoFavoritos != null && eventoFavoritos.size() > 0) {
             persistenceDao.deletaEventoFavoritoPorUID(evento.getUid(),persistenceDao.openDB(this));
            item.setIcon(android.R.drawable.btn_star_big_off);
            Log.i("DEBUG", "CHECK FALSE");
        } else {
            Log.i("UID",evento.getUid());
            persistenceDao.salvaEventoFavorito(evento, calendario, false,persistenceDao.openDB(this));
            item.setIcon(android.R.drawable.btn_star_big_on);
            Log.i("DEBUG", "CHECK TRUE");
        }

    }

    private void zoomImageFromThumb(final View thumbView, Drawable imageResId) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(R.id.expanded_image);
        expandedImageView.setImageDrawable(imageResId);
        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.coordinatorLayout).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;
        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator.ofFloat(expandedImageView,View.Y,startBounds.top))
                        .with(ObjectAnimator.ofFloat(expandedImageView,View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator .ofFloat(expandedImageView,  View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }
}
