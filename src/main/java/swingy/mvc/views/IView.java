package swingy.mvc.views;

public interface IView
{
    void    ChooseHero() throws Exception;
    void    drawGameObjects();
    void    viewRepaint();
    void    scrollPositionManager();
    void    updateData();
    void    addLog(String text);

    boolean simpleDialog(String message);
    String  get_Type();
    void    close();
}