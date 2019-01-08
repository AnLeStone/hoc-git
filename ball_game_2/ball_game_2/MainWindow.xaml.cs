using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Windows.Threading;

namespace ball_game_2
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();

            var timer = new DispatcherTimer();
            timer.Interval = TimeSpan.FromMilliseconds(10);
            timer.Start();
            timer.Tick += timer1_Tick;
            ball = new Ball(ball_1, 2, -2, this) ;
            list = new List<Rectangle>();

            createRects();
        }

        List<Rectangle> list;
        public float objectHeight = 10;
        public float objectWidth = 30;
        public float barHeight = 10;
        public float barWidth = 140;
        public float ballWidth = 20;
        Ball ball;

        private void createRects()
        {
            for (int i = 0; i < 10; i++)
            {
                for (int j = 0; j < 4; j++)
                {
                    Rectangle rec = new Rectangle();
                    rec.RadiusX = i * 50 + 50;
                    rec.RadiusY = j * 30 + 30;
                    rec.Fill = Brushes.Black;
                    rec.Width = objectWidth;
                    rec.Height = objectHeight;            
                    Canvas.SetTop(rec, objectWidth);
                    Canvas.SetLeft(rec, objectHeight);
                    list.Add(rec);
                    canvas.Children.Insert(i, rec);
                }
            }

        }

        //private void Form1_Load(object sender, EventArgs e)
        //{
        //    createRects();
        //    //Use double buffering to reduce flicker.
        //}

        //protected override void OnRender(DrawingContext e)
        //{ 
        //    SolidColorBrush brush = new SolidColorBrush(Colors.Black);
        //    Pen pen = new Pen(brush, 1);
            

        //    int len = list.Count;

        //    for (int i = 0; i < len; i++)
        //    {
        //        Rectangle rect = list[i];
        //        //e.DrawEllipse(Brushes.Yellow, pen, point, rect.x + objectWidth/2, rect.y + objectHeight/2);
        //        e.DrawRectangle(Brushes.Black, pen, new System.Windows.Rect(rect.x, rect.y, objectWidth, objectHeight));
        //    }

        //    //e.FillRectangle(Brushes.Orange, bar.x - barWidth / 2, bar.y, barWidth, barHeight);
        //    //e.Graphics.DrawRectangle(pen, bar.x - barWidth / 2, bar.y, barWidth, barHeight);
        //    e.DrawRectangle(Brushes.Black, pen, new System.Windows.Rect(bar.x, bar.y, objectWidth, objectHeight));

        //    //e.Graphics.FillEllipse(Brushes.Pink, ball.X, ball.Y,
        //    //       ball.width, ball.height);
        //    //e.Graphics.DrawEllipse(pen, ball.X, ball.Y,
        //    //    ball.width, ball.height);
        //    Point point = new Point(ball.X + Width/2, ball.Y + Width/2);
        //    e.DrawEllipse(Brushes.Pink, pen, point, point.X + ball.width/2, point.Y + ball.width/2);

        //}

        private void timer1_Tick(object sender, EventArgs e)
        {
            ball.X += ball.Vx;
            if (ball.X <= 0)
            {
                ball.Vx = -ball.Vx;
                Ball newBall = new Ball(ball.width, ball.height, 0, ball.Y, ball.Vx, ball.Vy, this);
                ball = newBall;
            }

            if (ball.X + ball.width >= (canvas.ActualWidth))
            {
                ball.Vx = -ball.Vx;
                Ball newBall = new Ball(ball.width, ball.height, canvas.ActualWidth - ball.width, ball.Y, ball.Vx, ball.Vy, this);
                ball = newBall;
            }

            ball.Y += ball.Vy;
            if (ball.Y <= 0)
            {
                ball.Vy = -ball.Vy;
                Ball newBall = new Ball(ball.width, ball.height, ball.X, 0, ball.Vx, ball.Vy, this);
                ball = newBall;
            }

            if (ball.Y + ball.height >= (canvas.ActualHeight))
            {
                ball.Vy = -ball.Vy;
                Ball newBall = new Ball(ball.width, ball.height, ball.X, canvas.ActualHeight - ball.height, ball.Vx, ball.Vy, this);
                ball = newBall;
            }

        }

        private void OnMouseMove(object sender, MouseEventArgs e)
        {
            Canvas.SetLeft(bar_1, e.GetPosition(this).X - bar_1.Width/2);
        }
    }

    public class Rect 
    {
        public double x;
        public double y;

        public Rect(double x, double y)
        {
            this.x = x;
            this.y = y;
        }

    }

    class Ball
    {
        public double width;
        public double height;
        private double x, y;   // Position.
        public double Vx, Vy; // Velocity.
        public Color color;
        private double ix, iy;
        private MainWindow window;

        public double X
        {
            get
            {
                return x;
            }

            set
            {
                x = value;
                Canvas.SetLeft(window.ball_1, x);
                this.ix = (x + width / 2);
            }
        }

        public double Y
        {
            get
            {
                return y;
            }

            set
            {
                y = value;
                Canvas.SetTop(window.ball_1, y);
                this.iy = (y + height / 2);
            }
        }

        public double Ix
        {
            get
            {
                return ix;
            }
        }

        public double Iy
        {
            get
            {
                return iy;
            }
        }

        public Ball(double w, double h, double x, double y, double vx, double vy, MainWindow window)
        {
            this.width = w;
            this.height = h;
            this.x = x;
            this.y = y;
            this.Vx = vx;
            this.Vy = vy;
            this.ix = (x + w / 2);
            this.iy = (y + w / 2);
            this.window = window;
        }

        public Ball(Ellipse ball, double vx, double vy, MainWindow window)
        {
            this.width = ball.Width;
            this.height = ball.Height;
            this.x = Canvas.GetLeft(ball);
            this.y = Canvas.GetTop(ball);
            this.Vx = vx;
            this.Vy = vy;
            this.color = Colors.Pink;
            this.ix = (x + width / 2);
            this.iy = (y + height / 2);
            this.window = window;
        }
    }
}
